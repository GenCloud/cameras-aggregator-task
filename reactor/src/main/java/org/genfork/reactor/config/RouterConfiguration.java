package org.genfork.reactor.config;

import lombok.RequiredArgsConstructor;
import org.genfork.model.CompleteCamData;
import org.genfork.reactor.service.ReactorDataAggregateService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Configuration
@RequiredArgsConstructor
public class RouterConfiguration {
	private final ReactorDataAggregateService dataAggregateService;

	@Bean
	public RouterFunction<ServerResponse> defaultAppRouter() {
		return route(GET("/data/readAll"), h -> ServerResponse
				.ok()
				.contentType(APPLICATION_JSON_UTF8)
				.body(dataAggregateService.readCompleteCamData(), CompleteCamData.class));
	}
}
