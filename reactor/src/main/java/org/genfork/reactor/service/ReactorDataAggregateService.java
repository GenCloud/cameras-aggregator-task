package org.genfork.reactor.service;

import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;
import reactor.core.publisher.Flux;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
public interface ReactorDataAggregateService {
	/**
	 * Request information from a resource and create a collection of objects
	 *
	 * @return flux stream of remapped data objects
	 */
	Flux<CompleteCamData> readCompleteCamData();

	/**
	 * Request base information of cam list from a resource and create a collection of objects
	 *
	 * @return flux stream
	 */
	Flux<CamListResponseData> readListCamData();
}
