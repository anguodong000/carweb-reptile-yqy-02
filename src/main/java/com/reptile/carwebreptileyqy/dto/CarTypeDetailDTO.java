package com.reptile.carwebreptileyqy.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class CarTypeDetailDTO implements Serializable {
    private static final long serialVersionUID = -8742074947015792578L;
    private int currentPage;
    private String carTypeId;
}
