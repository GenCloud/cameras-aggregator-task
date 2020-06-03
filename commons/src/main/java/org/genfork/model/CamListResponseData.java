package org.genfork.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Data
public class CamListResponseData implements Serializable {
	private Integer id;
	private String sourceDataUrl;
	private String tokenDataUrl;
}
