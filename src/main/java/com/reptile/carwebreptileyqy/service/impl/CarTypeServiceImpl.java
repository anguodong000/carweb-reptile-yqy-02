package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.dto.CarTypeDTO;
import com.reptile.carwebreptileyqy.entity.CarTypeEntity;
import com.reptile.carwebreptileyqy.mapper.CarTypeMapper;
import com.reptile.carwebreptileyqy.service.CarTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CarTypeServiceImpl implements CarTypeService {

    @Resource
    CarTypeMapper carTypeMapper;

    @Override
    public List<CarTypeEntity> list(CarTypeDTO carTypeDTO) {
        return carTypeMapper.list(carTypeDTO);
    }

    @Override
    public int total(CarTypeDTO carTypeDTO) {
        return carTypeMapper.total(carTypeDTO);
    }

    @Override
    public int add(CarTypeEntity carTypeEntity) {
        return carTypeMapper.add(carTypeEntity);
    }
}
