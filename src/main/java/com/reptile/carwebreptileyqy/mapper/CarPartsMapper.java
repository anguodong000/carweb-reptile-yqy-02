package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.dto.CarPartsDTO;
import com.reptile.carwebreptileyqy.entity.AutoPartsInfoEntity;
import com.reptile.carwebreptileyqy.entity.CarPartsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CarPartsMapper {

    @Insert("insert into car_parts_info(from_time,to_time,parts_no,parts_name,instruction,group_no,pnc,create_time)" +
            "values(#{fromTime},#{toTime},#{partsNo},#{partsName},#{instruction},#{groupNo},#{pnc},#{createTime})")
    int insertCarParts(CarPartsEntity carPartsEntity);

    List<CarPartsEntity> list(CarPartsDTO carPartsDTO);

    List<AutoPartsInfoEntity> autoPartsInfoList(CarPartsDTO carPartsDTO);

    int total(CarPartsDTO carPartsDTO);

    int autoPartsInfoTotal(CarPartsDTO carPartsDTO);

    List<CarPartsEntity> queryCarPartsByDetailId(CarPartsDTO carPartsDTO);

    int totalQueryCarPartsByDetailId(CarPartsDTO carPartsDTO);
}
