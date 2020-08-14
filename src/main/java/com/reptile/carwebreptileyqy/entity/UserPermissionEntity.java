package com.reptile.carwebreptileyqy.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户权限实体
 */
@Data
public class UserPermissionEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;

    private Integer userId;

    private String permissionName;

    private String permissionCode;
}
