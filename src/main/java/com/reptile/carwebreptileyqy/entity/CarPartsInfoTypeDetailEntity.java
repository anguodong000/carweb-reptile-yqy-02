package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarPartsInfoTypeDetailEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;

    private String id;

    private String carPartsInfoId;

    private String carTypeId;

    private String carTypeDetailId;
}
