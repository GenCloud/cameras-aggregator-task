package org.genfork.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: ngolubenko@context-it.ru
 * @created: 2020/06
 */
@Data
public class TokenResponseData implements Serializable {
	private String value;
	private Integer ttl;
}
