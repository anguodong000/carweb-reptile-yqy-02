package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author angd
 * @create 2019-08-28 16:25
 */
@Data
public class GraspRecordEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;

    private String id;
    private String partNo;
    private String partName;
    private String groupNo;
    private String pnc;
    private String startTime;
    private String endTime;
    private String instruction;

}
