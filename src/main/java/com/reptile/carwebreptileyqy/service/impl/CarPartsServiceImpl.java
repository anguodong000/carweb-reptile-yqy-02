package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.dto.CarPartsDTO;
import com.reptile.carwebreptileyqy.dto.PriceStatisticsDTO;
import com.reptile.carwebreptileyqy.dto.QueryPriceDto;
import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.AutoPartsInfoEntity;
import com.reptile.carwebreptileyqy.entity.CarPartsEntity;
import com.reptile.carwebreptileyqy.entity.GraspRecordEntity;
import com.reptile.carwebreptileyqy.mapper.CarPartsMapper;
import com.reptile.carwebreptileyqy.service.CarPartsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

@Service
@Slf4j
public class CarPartsServiceImpl implements CarPartsService {

    private static final String SUFFIX_2003 = ".xls";
    private static final String SUFFIX_2007 = ".xlsx";

    @Resource
    CarPartsMapper carPartsMapper;

    @Autowired
    SolrClient solrClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile file) {
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
            log.info(originalFilename);
            //throw new BusinessException(ReturnCode.CODE_FAIL, "格式错误");
        } else {
            //获取所有的工作表的的数量
            int numOfSheet = workbook.getNumberOfSheets();
            //遍历这个这些表
            for (int i = 0; i < numOfSheet; i++) {
                //获取一个sheet也就是一个工作簿
                Sheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                int lastRowNum = sheet.getLastRowNum();
                //从第一行开始第一行一般是标题
                for (int j = 1; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);
                    CarPartsEntity carPartsEntity = new CarPartsEntity();

                    if (row.getCell(0) != null) {
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        String from = row.getCell(0).getStringCellValue();
                        carPartsEntity.setFromTime(from);
                    }

                    if (row.getCell(1) != null&&!row.getCell(1).toString().equals("")) {
                        System.out.println("sheetName:"+sheetName+"j:"+j);
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                        String to = row.getCell(1).getStringCellValue();
                        carPartsEntity.setToTime(to);
                    }

                    if (row.getCell(2) != null) {
                        row.getCell(2).setCellType(Cell.CELL_TYPE_STRING);
                        String partsNo = row.getCell(2).getStringCellValue();
                        carPartsEntity.setPartsNo(partsNo);
                    }

                    if (row.getCell(3) != null) {
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        String partsName = row.getCell(3).getStringCellValue();
                        carPartsEntity.setPartsName(partsName);
                    }

                    if (row.getCell(4) != null) {
                        row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
                        String instruction = row.getCell(4).getStringCellValue();
                        carPartsEntity.setInstruction(instruction);
                    }

                    if (row.getCell(5) != null) {
                        row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
                        String groupNo = row.getCell(5).getStringCellValue();
                        carPartsEntity.setGroupNo(groupNo);
                    }

                    if (row.getCell(6) != null) {
                        row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
                        String pnc = row.getCell(6).getStringCellValue();
                        carPartsEntity.setPnc(pnc);
                    }
                    carPartsEntity.setCreateTime(new Date());
                    carPartsMapper.insertCarParts(carPartsEntity);
                }
            }
        }
        return "上传成功";
    }

    @Override
    public int updateParts(MultipartFile file) {
        int msg = 1;
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
            msg = 2;
            log.info(originalFilename);
            //throw new BusinessException(ReturnCode.CODE_FAIL, "格式错误");
        } else {
            //获取所有的工作表的的数量
            int numOfSheet = workbook.getNumberOfSheets();
            //遍历这个这些表
            for (int i = 0; i < numOfSheet; i++) {
                //获取一个sheet也就是一个工作簿
                Sheet sheet = workbook.getSheetAt(i);
                //检查表头
                Row rowTitle = sheet.getRow(0);
                if(StringUtils.isEmpty(rowTitle.getCell(0))||
                        StringUtils.isEmpty(rowTitle.getCell(1)) ||
                        StringUtils.isEmpty(rowTitle.getCell(2)) ){
                    msg = 0;
                    break;
                }
                if(!rowTitle.getCell(0).getStringCellValue().contains("商品编号") ||
                        !rowTitle.getCell(1).getStringCellValue().contains("名称") ||
                        !rowTitle.getCell(2).getStringCellValue().contains("价格") ){
                    msg = 0;
                    break;
                }
                int lastRowNum = sheet.getLastRowNum();
                //从第一行开始第一行一般是标题
                String productNumber = "";
                String productName = "";
                BigDecimal retailPrice = new BigDecimal(0);
                String priceChange = "";
                for (int j = 1; j <= lastRowNum; j++) {
                    Row row = sheet.getRow(j);

                    if (!StringUtils.isEmpty(row.getCell(0))) {
                        row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
                        productNumber = row.getCell(0).getStringCellValue();
                    }
                    if (!StringUtils.isEmpty(row.getCell(1))) {
                        row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
                        productName = row.getCell(1).getStringCellValue();
                    }
                    if (!StringUtils.isEmpty(row.getCell(2))) {
                        row.getCell(2).setCellType(Cell.CELL_TYPE_NUMERIC);
                        retailPrice = new BigDecimal(row.getCell(2).getNumericCellValue());
                    }
                    if (!StringUtils.isEmpty(row.getCell(3))) {
                        row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
                        priceChange = row.getCell(3).getStringCellValue();
                    }
                    try{
                        //查询配件是否存在
                        List<AutoPartsInfoEntity> queryEntityList = carPartsMapper.getPartByProductNumber(productNumber.trim());
                        if(!CollectionUtils.isEmpty(queryEntityList)){
                            for(AutoPartsInfoEntity queryEntity:queryEntityList){
                                AutoPartsInfoEntity autoPartsInfoEntity = new AutoPartsInfoEntity();
                                autoPartsInfoEntity.setId(queryEntity.getId());
                                autoPartsInfoEntity.setProductNumber(productNumber);
                                autoPartsInfoEntity.setProductName(queryEntity.getProductName());
                                autoPartsInfoEntity.setVehicleModel(queryEntity.getVehicleModel());
                                autoPartsInfoEntity.setWholesalePrice(queryEntity.getWholesalePrice());
                                autoPartsInfoEntity.setRetailPrice(retailPrice.doubleValue());
                                autoPartsInfoEntity.setDiscountPrice(queryEntity.getDiscountPrice());
                                autoPartsInfoEntity.setPriceChange(priceChange);
                                autoPartsInfoEntity.setUpdateTime(new Date());
                                //更新数据库
                                carPartsMapper.updatePartRetailPrice(autoPartsInfoEntity);
                                //添加到索引库
                                UpdateResponse updateResponse = solrClient.addBean(autoPartsInfoEntity);
                                solrClient.commit();
                                log.info("所以库更新成功：{}",updateResponse);
                            }
                        }else{
                            AutoPartsInfoEntity autoPartsInfoEntity = new AutoPartsInfoEntity();
                            autoPartsInfoEntity.setId(carPartsMapper.getPartMaxId());
                            autoPartsInfoEntity.setProductNumber(productNumber);
                            autoPartsInfoEntity.setProductName(productName);
                            autoPartsInfoEntity.setRetailPrice(retailPrice.doubleValue());
                            autoPartsInfoEntity.setPriceChange(priceChange);
                            autoPartsInfoEntity.setCreateTime(new Date());
                            carPartsMapper.savePartRetailPrice(autoPartsInfoEntity);
                            //添加到索引库
                            UpdateResponse updateResponse = solrClient.addBean(autoPartsInfoEntity);
                            log.info("所以库创建成功：{}",updateResponse);
                            solrClient.commit();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return msg;
    }

    @Override
    public int total(CarPartsDTO carPartsDTO) {
        return carPartsMapper.total(carPartsDTO);
    }

    @Override
    public List<CarPartsEntity> queryCarPartsByDetailId(CarPartsDTO carPartsDTO) {
        return carPartsMapper.queryCarPartsByDetailId(carPartsDTO);
    }

    @Override
    public int totalQueryCarPartsByDetailId(CarPartsDTO carPartsDTO) {
        return carPartsMapper.totalQueryCarPartsByDetailId(carPartsDTO);
    }

    @Override
    public List<AutoPartsInfoEntity> autoPartsInfoList(CarPartsDTO carPartsDTO) {
        return carPartsMapper.autoPartsInfoList(carPartsDTO);
    }

    @Override
    public int autoPartsInfoTotal(CarPartsDTO carPartsDTO) {
        return carPartsMapper.autoPartsInfoTotal(carPartsDTO);
    }

    @Override
    public int createPriceNeed(QueryPriceDto queryPriceDto) {
        queryPriceDto.setCreateTime(new Date());
        return carPartsMapper.createPriceNeed(queryPriceDto);
    }

    @Override
    public List<PriceStatisticsDTO> listPriceStatistics(UserDTO userDTO) {
        return carPartsMapper.listPriceStatistics(userDTO);
    }

    @Override
    public int priceStatisticsTotal(UserDTO userDTO) {
        return carPartsMapper.priceStatisticsTotal(userDTO);
    }

    @Override
    public List<PriceStatisticsDTO> listPriceStatisticsDetail(UserDTO userDTO) {
        return carPartsMapper.listPriceStatisticsDetail(userDTO);
    }

    @Override
    public int priceStatisticsDetailTotal(UserDTO userDTO) {
        return carPartsMapper.priceStatisticsDetailTotal(userDTO);
    }

    @Override
    public int clearData() {
        //查询所有创建时间和修改时间都为空重复数据
        List<AutoPartsInfoEntity> listRepeatParts = carPartsMapper.listRepeatParts();
        if(!CollectionUtils.isEmpty(listRepeatParts)){
            for(AutoPartsInfoEntity autoPartsInfoEntity : listRepeatParts){
                //根据编号查询重复具体配件信息
                Map<String,Object> params = new HashMap<>();
                params.put("productNumber",autoPartsInfoEntity.getProductNumber());
                List<AutoPartsInfoEntity> autoPartsInfoList = carPartsMapper.listParts(params);
                for(AutoPartsInfoEntity partsInfo : autoPartsInfoList){
                    //删除配件
                    carPartsMapper.deletePart(partsInfo.getId());
                    List<AutoPartsInfoEntity> autoPartsInfoList1 = carPartsMapper.listParts(params);
                    //剩余一条数据跳出循环
                    if(autoPartsInfoList1.size()==1){
                        break;
                    }
                }
            }
        }

        //查询创建时间或者修改时间不为空的重复数据
        List<AutoPartsInfoEntity> listRepeatParts1 = carPartsMapper.listRepeatParts1();
        if(!CollectionUtils.isEmpty(listRepeatParts1)){
            for(AutoPartsInfoEntity autoPartsInfoEntity1 : listRepeatParts1){
                //根据编号查询重复具体配件信息
                Map<String,Object> params1 = new HashMap<>();
                params1.put("productNumber",autoPartsInfoEntity1.getProductNumber());
                List<AutoPartsInfoEntity> autoPartsInfoList = carPartsMapper.listParts(params1);
                for(AutoPartsInfoEntity partsInfo : autoPartsInfoList){
                    //价格更新字段为空说明是旧数据
                    if(partsInfo.getPriceChange()==null){
                        //删除配件
                        carPartsMapper.deletePart(partsInfo.getId());
                        List<AutoPartsInfoEntity> autoPartsInfoList1 = carPartsMapper.listParts(params1);
                        //剩余一条数据跳出循环
                        if(autoPartsInfoList1.size()==1){
                            break;
                        }
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public int correctPrice() {
        //查询同意编号下价格不一样的数据
        List<AutoPartsInfoEntity> autoPartsInfoEntityList = carPartsMapper.listCorrectParts();
        if(!CollectionUtils.isEmpty(autoPartsInfoEntityList)){
            for (AutoPartsInfoEntity autoPartsInfoEntity:autoPartsInfoEntityList){
                //根据编号查询配件数据
                Map<String,Object> params = new HashMap<>();
                params.put("productNumber",autoPartsInfoEntity.getProductNumber());
                List<AutoPartsInfoEntity> partsList = carPartsMapper.listParts(params);
                double retailPrice = 0;
                String priceChange = "";
                boolean flag = false;
                if(!CollectionUtils.isEmpty(partsList)){
                    for(AutoPartsInfoEntity partNumber : partsList) {
                        if(!StringUtils.isEmpty(partNumber.getPriceChange())){
                            retailPrice = partNumber.getRetailPrice();
                            priceChange = partNumber.getPriceChange();
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        for(AutoPartsInfoEntity part : partsList) {
                            AutoPartsInfoEntity updatePartEntity = new AutoPartsInfoEntity();
                            BeanUtils.copyProperties(part,updatePartEntity);
                            updatePartEntity.setRetailPrice(retailPrice);
                            updatePartEntity.setPriceChange(priceChange);
                            updatePartEntity.setUpdateTime(new Date());
                            //更新数据库
                            carPartsMapper.updatePartRetailPrice(updatePartEntity);
                            //更新索引库
                            try{
                                UpdateResponse updateResponse = solrClient.addBean(updatePartEntity);
                                solrClient.commit();
                                log.info("索引库更新成功：{}",updateResponse);
                            }catch (Exception e){
                                log.info("索引库更新失败");
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public void exportExcel(HttpServletRequest request,HttpServletResponse response, UserDTO userDTO) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("配件数据");
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,15*256);
        sheet.setColumnWidth(1,15*256);
        sheet.setColumnWidth(2,25*256);
        sheet.setColumnWidth(3,40*256);
        sheet.setColumnWidth(4,70*256);
        sheet.setColumnWidth(5,15*256);
        sheet.setColumnWidth(6,15*256);
        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("配件编号");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue("配件名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("价格");
        cell.setCellStyle(style);

        //获取数据
        List<PriceStatisticsDTO> priceStatisticsDTOList = carPartsMapper.listPriceStatisticsDetail(userDTO);

        int rowNum=1;
        for(PriceStatisticsDTO priceStatisticsDTO:priceStatisticsDTOList){
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(priceStatisticsDTO.getProductNumber());
            row1.createCell(1).setCellValue(priceStatisticsDTO.getProductName());
            row1.createCell(2).setCellValue(Double.parseDouble(priceStatisticsDTO.getRetailPrice().toString()));
            rowNum++;
        }
        String fileName = "配件数据导出.xls";
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            workbook.write(fos);
            fos.flush();
            fos.close();

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
