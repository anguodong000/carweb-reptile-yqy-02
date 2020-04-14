package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CarHingesInfoEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;
    private String id;
    private String hingesNumber;
    private String vehicleModel;
    private String carDoorPosition;
    private String installPosition;
}
