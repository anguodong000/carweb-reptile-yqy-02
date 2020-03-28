package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarTypeDetailEntity implements Serializable {

    private static final long serialVersionUID = -8742074947015792578L;

    private String id;

    private String carTypeId;

    private String displacement;

    private String inletModel;

    private String createYear;

    private String environmentalStandards;
}
