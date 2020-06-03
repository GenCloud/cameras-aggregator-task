package org.genfork;

import org.genfork.futures.AppInit;
import org.genfork.model.CompleteCamData;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppInit.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataControllerTest extends Assert {
	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void t01_testReadCompleteData() {
		final ResponseEntity<CompleteCamData[]> entity = testRestTemplate.getForEntity("/data/readAll", CompleteCamData[].class);
		final CompleteCamData[] body = entity.getBody();
		assertNotNull(body);
		assertEquals(4, body.length);
		assertTrue(Arrays.stream(body).anyMatch(c -> c.getId().equals(2)));
		assertTrue(Arrays.stream(body).noneMatch(c -> StringUtils.isEmpty(c.getValue())));
		assertTrue(Arrays.stream(body).noneMatch(c -> StringUtils.isEmpty(c.getVideoUrl())));
	}
}
