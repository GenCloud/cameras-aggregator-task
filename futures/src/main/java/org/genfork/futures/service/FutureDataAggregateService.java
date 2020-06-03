package org.genfork.futures.service;

import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
public interface FutureDataAggregateService {
	CompletableFuture<List<CompleteCamData>> readCompleteCamData();

	CompletableFuture<List<CamListResponseData>> readListCamData();
}
