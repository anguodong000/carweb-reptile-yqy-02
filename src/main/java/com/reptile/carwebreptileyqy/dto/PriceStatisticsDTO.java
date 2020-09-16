package com.reptile.carwebreptileyqy.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PriceStatisticsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String productNumber;

	private String productName;

	private String vehicleModel;

	private String company;

	private Integer count;

	private BigDecimal retailPrice;
}
