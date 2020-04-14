package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.dto.CarHingesDTO;
import com.reptile.carwebreptileyqy.entity.CarHingesInfoEntity;
import com.reptile.carwebreptileyqy.mapper.CarHingesMapper;
import com.reptile.carwebreptileyqy.service.CarHingesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class CarHingesServiceImpl implements CarHingesService {

    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";

    @Autowired
    CarHingesMapper carHingesMapper;

    @Override
    public List<CarHingesInfoEntity> list(CarHingesDTO carHingesDTO) {
        return carHingesMapper.list(carHingesDTO);
    }

    @Override
    public int total(CarHingesDTO carHingesDTO) {
        return carHingesMapper.total(carHingesDTO);
    }

    @Override
    public String importCarHingesInfo(MultipartFile file) {
        //获取文件的名字
        String originalFilename = file.getOriginalFilename();
        Workbook workbook = null;
        try {
            if (originalFilename.endsWith(SUFFIX_2003)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if (originalFilename.endsWith(SUFFIX_2007)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            }
        } catch (Exception e) {
            log.info(originalFilename);
            e.printStackTrace();
        }
        if (workbook == null) {
            log.info("文件格式错误！{}",originalFilename);
            //throw new ServiceException("202", "格式错误");
        } else {
            //获取所有的工作表的的数量
            int numOfSheet = workbook.getNumberOfSheets();
            //遍历这个这些表
            for (int i = 0; i < 1; i++) {
                //获取一个sheet也就是一个工作簿
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                int lastRowNum = sheet.getLastRowNum();
                //从第二行开始第一行一般是标题
                for (int j = 1; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);
                    CarHingesInfoEntity carHingesInfoEntity = new CarHingesInfoEntity();

                    if (!StringUtils.isEmpty(row.getCell(0))) {
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        String vehicleModel = row.getCell(0).getStringCellValue().trim();
                        carHingesInfoEntity.setVehicleModel(vehicleModel);
                    }

                    if (!StringUtils.isEmpty(row.getCell(1))) {
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                        String carDoorPosition = row.getCell(1).getStringCellValue().trim();
                        carHingesInfoEntity.setCarDoorPosition(carDoorPosition);
                    }

                    if (!StringUtils.isEmpty(row.getCell(2))) {
                        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                        String installPosition = row.getCell(2).getStringCellValue().trim();
                        carHingesInfoEntity.setInstallPosition(installPosition);
                    }

                    if (!StringUtils.isEmpty(row.getCell(3))) {
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        String hingesNumber = row.getCell(3).getStringCellValue().trim();
                        carHingesInfoEntity.setHingesNumber(hingesNumber);
                    }
                    carHingesMapper.insertCarHingesInfo(carHingesInfoEntity);
                }
            }
        }
        return "上传成功";
    }
}
