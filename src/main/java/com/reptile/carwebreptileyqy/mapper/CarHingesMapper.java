package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.dto.CarHingesDTO;
import com.reptile.carwebreptileyqy.entity.CarHingesInfoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CarHingesMapper {

    @Insert("insert into car_hinges_info(hinges_number,vehicle_model,car_door_position,install_position)" +
            "values(#{hingesNumber},#{vehicleModel},#{carDoorPosition},#{installPosition})")
    int insertCarHingesInfo(CarHingesInfoEntity carHingesInfoEntity);

    List<CarHingesInfoEntity> list(CarHingesDTO carHingesDTO);

    int total(CarHingesDTO carHingesDTO);
}
