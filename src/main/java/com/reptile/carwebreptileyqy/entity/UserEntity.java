package com.reptile.carwebreptileyqy.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserEntity implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;
    private int id;
    private String username;
    private String password;
    private String telephone;
    private String email;
    private Date createTime;
    private String company;
    private String companyAddress;
}
