package org.genfork.futures.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Configuration
@EnableAsync
public class ExecutorConfiguration {
	@Bean
	public Executor taskExecutor() {
		return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	}
}
