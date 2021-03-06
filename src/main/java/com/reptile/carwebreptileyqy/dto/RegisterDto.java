package com.reptile.carwebreptileyqy.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;
    private int userId;
    private String telephone;
    private String username;
    private String password;
    private String email;
    private String company;
    private String companyAddress;
}
