package org.genfork.futures.service;

import lombok.RequiredArgsConstructor;
import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;
import org.genfork.model.SourceResponseData;
import org.genfork.model.TokenResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Service
@RequiredArgsConstructor
public class FutureDataAggregateServiceImpl implements FutureDataAggregateService {
	private final String baseUrl = "http://www.mocky.io";

	private final RestTemplate restTemplate;

	@Override
	public CompletableFuture<List<CompleteCamData>> readCompleteCamData() {
		return readListCamData().thenApply(wrap -> wrap
				.stream()
				.map(data -> buildCompleteData(data).join())
				.collect(Collectors.toList()));
	}

	@Override
	public CompletableFuture<List<CamListResponseData>> readListCamData() {
		return CompletableFuture.supplyAsync(() -> {
			final ResponseEntity<CamListResponseData[]> entity = restTemplate.getForEntity(baseUrl + "/v2/5c51b9dd3400003252129fb5", CamListResponseData[].class);
			return entity.getStatusCode().is2xxSuccessful() && entity.hasBody() ? Arrays.stream(Objects.requireNonNull(entity.getBody())).collect(Collectors.toList()) : null;
		});
	}

	private <T extends Serializable> CompletableFuture<T> readCommonData(String uri, Class<T> type) {
		return CompletableFuture.supplyAsync(() -> {
			final ResponseEntity<T> entity = restTemplate.getForEntity(uri, type);
			return entity.getStatusCode().is2xxSuccessful() && entity.hasBody() ? entity.getBody() : null;
		});
	}

	private CompletableFuture<CompleteCamData> buildCompleteData(CamListResponseData data) {
		final String sourceDataUrl = data.getSourceDataUrl();
		final String tokenDataUrl = data.getTokenDataUrl();

		final CompletableFuture<SourceResponseData> sourceResponseDataMono = readCommonData(sourceDataUrl, SourceResponseData.class);
		final CompletableFuture<TokenResponseData> tokenResponseDataMono = readCommonData(tokenDataUrl, TokenResponseData.class);

		return CompletableFuture.allOf(sourceResponseDataMono, tokenResponseDataMono)
				.thenApply(voidData -> combine(data, sourceResponseDataMono.join(), tokenResponseDataMono.join()));
	}

	private CompleteCamData combine(CamListResponseData camListResponseData, SourceResponseData sourceResponseData,
									TokenResponseData tokenResponseData) {
		final CompleteCamData camData = new CompleteCamData();
		camData.setId(camListResponseData.getId());
		camData.setUrlType(sourceResponseData.getUrlType());
		camData.setVideoUrl(sourceResponseData.getVideoUrl());
		camData.setValue(tokenResponseData.getValue());
		camData.setTtl(tokenResponseData.getTtl());
		return camData;
	}
}
