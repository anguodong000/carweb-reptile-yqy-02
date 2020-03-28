package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author angd
 * @create 2019-08-29 17:12
 */
@Data
public class ErrorRecordEntity implements Serializable {

    private static final long serialVersionUID = -8742074947015792578L;

    private String id;

    private String exceptionPnc;
}
