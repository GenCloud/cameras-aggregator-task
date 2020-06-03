package org.genfork.model;

import lombok.Data;
import org.genfork.model.enums.UrlType;

import java.io.Serializable;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Data
public class CompleteCamData implements Serializable {
	private Integer id;
	private UrlType urlType;
	private String videoUrl;
	private String value;
	private Integer ttl;
}
