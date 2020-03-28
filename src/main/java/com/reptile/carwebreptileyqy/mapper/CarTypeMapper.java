package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.dto.CarTypeDTO;
import com.reptile.carwebreptileyqy.entity.CarTypeEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarTypeMapper {

    List<CarTypeEntity> list(CarTypeDTO carTypeDTO);

    int total(CarTypeDTO carTypeDTO);

    @Insert("insert into car_type(car_line,car_brand,car_type_name,level)" +
            "values(#{carLine},#{carBrand},#{carTypeName},#{level})")
    int add(CarTypeEntity carTypeEntity);
}
