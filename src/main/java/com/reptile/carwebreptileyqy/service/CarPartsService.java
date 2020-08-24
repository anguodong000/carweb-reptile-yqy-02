package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.CarPartsDTO;
import com.reptile.carwebreptileyqy.dto.QueryPriceDto;
import com.reptile.carwebreptileyqy.entity.AutoPartsInfoEntity;
import com.reptile.carwebreptileyqy.entity.CarPartsEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarPartsService {
    String upload(MultipartFile file);

    List<CarPartsEntity> list(CarPartsDTO carPartsDTO);

    List<AutoPartsInfoEntity> autoPartsInfoList(CarPartsDTO carPartsDTO);

    int total(CarPartsDTO carPartsDTO);

    int autoPartsInfoTotal(CarPartsDTO carPartsDTO);

    List<CarPartsEntity> queryCarPartsByDetailId(CarPartsDTO carPartsDTO);

    int totalQueryCarPartsByDetailId(CarPartsDTO carPartsDTO);

    int createPriceNeed(QueryPriceDto queryPriceDto);

    String updateParts(MultipartFile file);
}
