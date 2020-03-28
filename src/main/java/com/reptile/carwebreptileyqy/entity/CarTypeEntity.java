package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarTypeEntity implements Serializable {

    private static final long serialVersionUID = -8742074947015792578L;

    private String id;

    /**
     * 车系
     */
    private String carLine;

    /**
     * 品牌
     */
    private String carBrand;

    /**
     * 车型名称
     */
    private String carTypeName;

    /**
     * 级别
     */
    private String level;
}
