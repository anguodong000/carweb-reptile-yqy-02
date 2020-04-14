package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.CarHingesDTO;
import com.reptile.carwebreptileyqy.entity.CarHingesInfoEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CarHingesService {

    String importCarHingesInfo(MultipartFile file);

    List<CarHingesInfoEntity> list(CarHingesDTO carHingesDTO);

    int total(CarHingesDTO carHingesDTO);
}
