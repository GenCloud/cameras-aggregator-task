package org.genfork;

import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;
import org.genfork.reactor.AppInit;
import org.genfork.reactor.service.ReactorDataAggregateService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInit.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataAggregateTest {
	@Autowired
	private ReactorDataAggregateService reactorDataAggregateService;

	@Test
	public void t01_testReadCamList() {
		final Flux<CamListResponseData> camListResponseDataFlux = reactorDataAggregateService.readListCamData();
		StepVerifier.create(camListResponseDataFlux)
				.thenConsumeWhile(c ->
						MockUtils.getCamListData().stream().anyMatch(d -> d.getId().equals(c.getId())))
				.expectComplete()
				.verify();
	}

	@Test
	public void t02_testCompleteReadData() {
		final Flux<CompleteCamData> completeCamDataFlux = reactorDataAggregateService.readCompleteCamData();
		StepVerifier.create(completeCamDataFlux)
				.thenConsumeWhile(c ->
						MockUtils.getCompleteCamData().stream().noneMatch(d -> d.getId().equals(c.getId())))
				.thenConsumeWhile(c ->
						!StringUtils.isEmpty(c.getValue()) && StringUtils.isEmpty(c.getVideoUrl()))
				.thenConsumeWhile(c ->
						c.getTtl() != null)
				.expectComplete()
				.verify();
	}
}
