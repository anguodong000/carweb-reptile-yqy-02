package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.AddCarDetailPartsInfoDTO;
import com.reptile.carwebreptileyqy.dto.CarTypeDetailDTO;
import com.reptile.carwebreptileyqy.entity.CarPartsInfoTypeDetailEntity;
import com.reptile.carwebreptileyqy.entity.CarTypeDetailEntity;
import java.util.List;

public interface CarTypeDetailService {
    List<CarTypeDetailEntity> list(CarTypeDetailDTO carTypeDTO);

    int total(CarTypeDetailDTO carTypeDTO);

    int add(CarTypeDetailEntity carTypeDetailEntity);

    int addCarPartsInfo(CarPartsInfoTypeDetailEntity carPartsInfoTypeDetailEntity);
}
