package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CarPartsEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;

    private String id;
    private String fromTime;
    private String toTime;
    private String partsNo;
    private String partsName;
    private String instruction;
    private String groupNo;
    private String pnc;
    private Date createTime;
}
