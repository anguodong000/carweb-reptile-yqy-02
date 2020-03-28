package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.dto.AddCarDetailPartsInfoDTO;
import com.reptile.carwebreptileyqy.dto.CarTypeDetailDTO;
import com.reptile.carwebreptileyqy.entity.CarPartsInfoTypeDetailEntity;
import com.reptile.carwebreptileyqy.entity.CarTypeDetailEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CarTypeDetailMapper {

    List<CarTypeDetailEntity> list(CarTypeDetailDTO carTypeDTO);

    int total(CarTypeDetailDTO carTypeDTO);

    @Insert("insert into car_type_detail(car_type_id,displacement,inlet_model,create_year,environmental_standards)" +
            "values(#{carTypeId},#{displacement},#{inletModel},#{createYear},#{environmentalStandards})")
    int add(CarTypeDetailEntity carTypeDetailEntity);

    @Insert("insert into car_parts_info_type_detail(car_parts_info_id,car_type_id,car_type_detail_id)" +
            "values(#{carPartsInfoId},#{carTypeId},#{carTypeDetailId})")
    int addCarPartsInfo(CarPartsInfoTypeDetailEntity carPartsInfoTypeDetailEntity);
}
