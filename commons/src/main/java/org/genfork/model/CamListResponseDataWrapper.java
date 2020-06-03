package org.genfork.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Data
public class CamListResponseDataWrapper implements Serializable {
	private final List<CamListResponseData> camListResponseData = new ArrayList<>();
}
