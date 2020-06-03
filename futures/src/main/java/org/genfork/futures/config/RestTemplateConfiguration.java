package org.genfork.futures.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Configuration
@EnableScheduling
@EnableAsync
public class RestTemplateConfiguration {
	private final static int MAX_ROUTE_CONNECTIONS = 40;
	private final static int MAX_TOTAL_CONNECTIONS = 40;

	private final static int DEFAULT_KEEP_ALIVE_TIME = 20 * 1000;

	private final static int CONNECT_TIMEOUT = 30 * 1000;
	private final static int REQUEST_TIMEOUT = 30 * 1000;
	private final static int SOCKET_TIMEOUT = 60 * 1000;

	private final static int IDLE_CONNECTION_WAIT_TIME = 30 * 1000;

	@Bean
	public PoolingHttpClientConnectionManager poolingConnectionManager() {
		final PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
		poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		poolingConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
		return poolingConnectionManager;
	}

	@Bean
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return (httpResponse, httpContext) -> {
			final HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
			final HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

			while (elementIterator.hasNext()) {
				final HeaderElement element = elementIterator.nextElement();
				final String param = element.getName();
				final String value = element.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					return Long.parseLong(value) * 1000;
				}
			}

			return DEFAULT_KEEP_ALIVE_TIME;
		};

	}

	@Bean
	public CloseableHttpClient closeableHttpClient() {
		final RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setConnectionRequestTimeout(REQUEST_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT)
				.build();
		return HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManager(poolingConnectionManager())
				.setKeepAliveStrategy(connectionKeepAliveStrategy())
				.build();
	}

	@Bean
	public ClientHttpRequestFactory clientHttpRequestFactory(CloseableHttpClient closeableHttpClient) {
		final HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(closeableHttpClient);
		return clientHttpRequestFactory;
	}

	@Bean
	public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
		return new RestTemplateBuilder().requestFactory(() -> clientHttpRequestFactory).build();
	}

	@Bean
	public TaskScheduler taskScheduler() {
		final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("idleMonitor");
		scheduler.setPoolSize(5);
		return scheduler;
	}

	@Bean
	public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager pool) {
		return new Runnable() {
			@Override
			@Scheduled(fixedDelay = 20000)
			public void run() {
				if (pool != null) {
					pool.closeExpiredConnections();
					pool.closeIdleConnections(IDLE_CONNECTION_WAIT_TIME, TimeUnit.MILLISECONDS);
				}
			}
		};
	}
}
