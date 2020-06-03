package org.genfork;

import org.genfork.reactor.AppInit;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInit.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "10000")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataControllerTest {
	private final List<Runnable> runnables = new ArrayList<>();
	@Autowired
	private WebTestClient webTestClient;

	@Before
	public void beforeInit() {
		doGenerateTasks();
	}

	private void doGenerateTasks() {
		IntStream.range(0, 100).<Runnable>mapToObj(i -> () -> webTestClient.get()
				.uri("/data/readAll")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(4)
				.jsonPath("$[0].id").isEqualTo(2)).forEach(runnables::add);
	}

	@Test
	public void t01_testReadCompleteData() {
		webTestClient.get()
				.uri("/data/readAll")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.length()").isEqualTo(4)
				.jsonPath("$[0].id").isEqualTo(2);
	}

	@Test
	public void t02_testRunnableFutures() {
		final CompletableFuture<?>[] futures = runnables.stream().map(CompletableFuture::runAsync).toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();
	}
}
