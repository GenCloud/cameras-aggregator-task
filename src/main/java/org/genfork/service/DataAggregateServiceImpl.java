package org.genfork.service;

import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;
import org.genfork.model.SourceResponseData;
import org.genfork.model.TokenResponseData;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Service
public class DataAggregateServiceImpl implements DataAggregateService {
	private final String baseUrl = "http://www.mocky.io";

	private final WebClient baseClient = WebClient
			.builder()
			.baseUrl(baseUrl)
			.build();

	@Override
	public Flux<CompleteCamData> readCompleteCamData() {
		return readListCamData()
				.flatMap(this::buildCompleteData);
	}

	@Override
	public Flux<CamListResponseData> readListCamData() {
		return baseClient.get()
				.uri(uriBuilder ->
						uriBuilder.pathSegment("v2", "5c51b9dd3400003252129fb5").build())
				.retrieve()
				.bodyToFlux(CamListResponseData.class);
	}

	private <T extends Serializable> Mono<T> readCommonData(String uri, Class<T> type) {
		return WebClient
				.builder()
				.baseUrl(uri)
				.build()
				.get()
				.retrieve()
				.bodyToMono(type);
	}

	private Publisher<? extends CompleteCamData> buildCompleteData(CamListResponseData data) {
		final String sourceDataUrl = data.getSourceDataUrl();
		final String tokenDataUrl = data.getTokenDataUrl();

		final Mono<SourceResponseData> sourceResponseDataMono = readCommonData(sourceDataUrl, SourceResponseData.class);
		final Mono<TokenResponseData> tokenResponseDataMono = readCommonData(tokenDataUrl, TokenResponseData.class);

		return Mono.zip(sourceResponseDataMono,
				tokenResponseDataMono)
				.map(tuple -> {
					final SourceResponseData t1 = tuple.getT1();
					final TokenResponseData t2 = tuple.getT2();

					final CompleteCamData camData = new CompleteCamData();
					camData.setId(data.getId());
					camData.setUrlType(t1.getUrlType());
					camData.setVideoUrl(t1.getVideoUrl());
					camData.setValue(t2.getValue());
					camData.setTtl(t2.getTtl());
					return camData;
				});
	}
}
