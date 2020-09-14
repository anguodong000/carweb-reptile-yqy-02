package com.reptile.carwebreptileyqy.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class PriceStatisticsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String company;

	private Integer count;
}
