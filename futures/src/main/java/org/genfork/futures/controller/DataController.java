package org.genfork.futures.controller;

import lombok.RequiredArgsConstructor;
import org.genfork.futures.service.FutureDataAggregateService;
import org.genfork.model.CompleteCamData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@RestController
@RequestMapping("/data")
@RequiredArgsConstructor
public class DataController {
	private final FutureDataAggregateService futureDataAggregateService;

	@GetMapping("/readAll")
	@Async
	public CompletableFuture<List<CompleteCamData>> readAllData() {
		return futureDataAggregateService.readCompleteCamData();
	}
}
