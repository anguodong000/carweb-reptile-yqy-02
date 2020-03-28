package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.dto.AddCarDetailPartsInfoDTO;
import com.reptile.carwebreptileyqy.dto.CarTypeDetailDTO;
import com.reptile.carwebreptileyqy.entity.CarPartsInfoTypeDetailEntity;
import com.reptile.carwebreptileyqy.entity.CarTypeDetailEntity;
import com.reptile.carwebreptileyqy.mapper.CarTypeDetailMapper;
import com.reptile.carwebreptileyqy.service.CarTypeDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CarTypeDetailServiceImpl implements CarTypeDetailService {

    @Resource
    CarTypeDetailMapper carTypeDetailMapper;

    @Override
    public List<CarTypeDetailEntity> list(CarTypeDetailDTO carTypeDTO) {
        return carTypeDetailMapper.list(carTypeDTO);
    }

    @Override
    public int total(CarTypeDetailDTO carTypeDTO) {
        return carTypeDetailMapper.total(carTypeDTO);
    }

    @Override
    public int add(CarTypeDetailEntity carTypeDetailEntity) {
        return carTypeDetailMapper.add(carTypeDetailEntity);
    }

    @Override
    public int addCarPartsInfo(CarPartsInfoTypeDetailEntity carPartsInfoTypeDetailEntity) {
        return carTypeDetailMapper.addCarPartsInfo(carPartsInfoTypeDetailEntity);
    }
}
