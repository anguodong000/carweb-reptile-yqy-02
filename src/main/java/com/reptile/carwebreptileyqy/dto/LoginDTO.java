package com.reptile.carwebreptileyqy.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;
    private String username;
    private String password;
}
