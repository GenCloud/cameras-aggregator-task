package org.genfork.reactor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@SpringBootApplication
public class AppInit {
	public static void main(String[] args) {
		Hooks.onOperatorDebug();
		SpringApplication.run(AppInit.class, args);
	}
}
