package com.reptile.carwebreptileyqy.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class UserDTO implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;
    private int currentPage;
    private String username;
    private String telephone;
    private String password;
    private Integer isAuthority;
    private Date updateTime;
    private String email;
    private String company;
    private String validateCode;
}
