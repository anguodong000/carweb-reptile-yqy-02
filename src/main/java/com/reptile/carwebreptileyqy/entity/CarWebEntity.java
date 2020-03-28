package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CarWebEntity implements Serializable {

    private static final long serialVersionUID = -8742074947015792578L;

    private String id;
    private String carType;
    private String partNo;
    private String groupNo;
    private String type;
    private String suc;
    private String pnc;
    private String chName;
    private String enName;
    private String itc;
    private String newPartNo;
    private int amountUsed;
    private String engine;
    private String transmission;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String option5;
    private String option6;
    private String option7;
    private String option8;
    private String option9;
    private String option10;
    private String option11;
    private String option12;
    private String option13;
    private String option14;
    private String exclusiveOp1;
    private String remark;
    private Date createTime;
    private String exceptionPnc;
}
