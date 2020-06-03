package org.genfork.model;

import lombok.Data;
import org.genfork.model.enums.UrlType;

import java.io.Serializable;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Data
public class SourceResponseData implements Serializable {
	private UrlType urlType;
	private String videoUrl;
}
