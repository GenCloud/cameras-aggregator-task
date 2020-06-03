package org.genfork;

import org.genfork.futures.AppInit;
import org.genfork.futures.service.FutureDataAggregateService;
import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInit.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataAggregateTest extends Assert {
	@Autowired
	private FutureDataAggregateService futureDataAggregateService;

	@Test
	public void t01_testReadCamList() {
		final CompletableFuture<List<CamListResponseData>> camListResponseDataFuture = futureDataAggregateService.readListCamData();
		final List<CamListResponseData> collection = camListResponseDataFuture.join();
		assertFalse(CollectionUtils.isEmpty(collection));
		assertEquals(collection, MockUtils.getCamListData());
	}

	@Test
	public void t02_testCompleteReadData() {
		final CompletableFuture<List<CompleteCamData>> completeCamDataFuture = futureDataAggregateService.readCompleteCamData();
		final List<CompleteCamData> collection = completeCamDataFuture.join();
		assertFalse(CollectionUtils.isEmpty(collection));
		assertTrue(collection.stream().noneMatch(c -> StringUtils.isEmpty(c.getValue())));
		assertTrue(collection.stream().noneMatch(c -> StringUtils.isEmpty(c.getVideoUrl())));
		assertTrue(collection.stream().anyMatch(c -> c.getId().equals(MockUtils.getCompleteCamData().get(0).getId())));
	}
}
