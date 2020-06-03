package org.genfork;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.genfork.model.CamListResponseData;
import org.genfork.model.CompleteCamData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@UtilityClass
public class MockUtils {
	private final ObjectMapper objectMapper = new ObjectMapper();

	private List<CamListResponseData> camListData;
	private List<CompleteCamData> completeCamData;

	/**
	 * Read standalone camera list and convert it to list of objects for test
	 *
	 * @return list objects
	 */
	public List<CamListResponseData> getCamListData() {
		if (camListData == null) {
			final Resource camListJson = new ClassPathResource("mock_cam_list.json");
			try {
				return camListData = objectMapper.readValue(camListJson.getInputStream(), new TypeReference<List<CamListResponseData>>() {
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return camListData;
	}

	/**
	 * Read standalone camera list result data and convert it to list of objects for test
	 *
	 * @return list objects
	 */
	public List<CompleteCamData> getCompleteCamData() {
		if (completeCamData == null) {
			final Resource completeJson = new ClassPathResource("mock_result_data.json");
			try {
				return completeCamData = objectMapper.readValue(completeJson.getInputStream(), new TypeReference<List<CompleteCamData>>() {
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return completeCamData;
	}
}
