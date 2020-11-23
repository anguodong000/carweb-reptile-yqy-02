package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.CarPartsDTO;
import com.reptile.carwebreptileyqy.dto.PriceStatisticsDTO;
import com.reptile.carwebreptileyqy.dto.QueryPriceDto;
import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.AutoPartsInfoEntity;
import com.reptile.carwebreptileyqy.entity.CarPartsEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CarPartsService {
    String upload(MultipartFile file);

    List<AutoPartsInfoEntity> autoPartsInfoList(CarPartsDTO carPartsDTO);

    int total(CarPartsDTO carPartsDTO);

    int autoPartsInfoTotal(CarPartsDTO carPartsDTO);

    List<CarPartsEntity> queryCarPartsByDetailId(CarPartsDTO carPartsDTO);

    int totalQueryCarPartsByDetailId(CarPartsDTO carPartsDTO);

    int createPriceNeed(QueryPriceDto queryPriceDto);

    int updateParts(MultipartFile file);

    List<PriceStatisticsDTO> listPriceStatistics(UserDTO userDTO);

    int priceStatisticsTotal(UserDTO userDTO);

    List<PriceStatisticsDTO> listPriceStatisticsDetail(UserDTO userDTO);

    int priceStatisticsDetailTotal(UserDTO userDTO);

    int clearData();

    int correctPrice();

    void exportExcel(HttpServletResponse response,UserDTO userDTO);
}
