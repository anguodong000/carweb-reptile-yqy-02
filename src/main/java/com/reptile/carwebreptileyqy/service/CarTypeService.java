package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.CarTypeDTO;
import com.reptile.carwebreptileyqy.entity.CarTypeEntity;

import java.util.List;

public interface CarTypeService {

    List<CarTypeEntity> list(CarTypeDTO carTypeDTO);

    int total(CarTypeDTO carTypeDTO);

    int add(CarTypeEntity carTypeEntity);
}
