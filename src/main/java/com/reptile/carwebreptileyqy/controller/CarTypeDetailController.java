package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.AddCarDetailPartsInfoDTO;
import com.reptile.carwebreptileyqy.dto.CarTypeDTO;
import com.reptile.carwebreptileyqy.dto.CarTypeDetailDTO;
import com.reptile.carwebreptileyqy.entity.CarPartsInfoTypeDetailEntity;
import com.reptile.carwebreptileyqy.entity.CarTypeDetailEntity;
import com.reptile.carwebreptileyqy.entity.CarTypeEntity;
import com.reptile.carwebreptileyqy.service.CarTypeDetailService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author angd
 * @create 2020-3-14
 */
@Controller
@Slf4j
public class CarTypeDetailController {

    @Resource
    CarTypeDetailService carTypeDetailService;

    @RequestMapping("/carTypeDetail/addDetailHtml")
    public String add(ModelMap modelMap) {
        return "cartype/add_detail";
    }

    /**
     * 车型列表查询
     * @param carTypeDetailDTO
     * @return
     */
    @PostMapping(value = "/carTypeDetail/list",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse listAll(
            @RequestBody CarTypeDetailDTO carTypeDetailDTO){
        BaseResponse baseResponse = new BaseResponse();
        Map map = new LinkedHashMap();
        List<CarTypeDetailEntity> list = carTypeDetailService.list(carTypeDetailDTO);
        int total = carTypeDetailService.total(carTypeDetailDTO);
        map.put("total",total);
        map.put("currentPage",carTypeDetailDTO.getCurrentPage());
        map.put("carTypeDetailList",list);
        baseResponse.setData(map);
        return baseResponse;
    }

    /**
     * 添加
     * @param carTypeDetailEntity
     * @return
     */
    @PostMapping(value = "/carTypeDetail/add",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse add(
            @RequestBody CarTypeDetailEntity carTypeDetailEntity){
        BaseResponse baseResponse = new BaseResponse();
        try{
            int flag = carTypeDetailService.add(carTypeDetailEntity);
            if(flag>0){
                baseResponse.setData("保存成功!");
            }else{
                baseResponse.setData("保存失败!");
            }
        }catch (Exception e){
            log.info("保存车型信息异常!");
            baseResponse.setData("保存车型信息异常!");
        }
        return baseResponse;
    }

    /**
     * 添加配件
     * @param addCarDetailPartsInfoDTO
     * @return
     */
    @PostMapping(value = "/carTypeDetail/addCarPartsInfo",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse addCarPartsInfo(
            @RequestBody AddCarDetailPartsInfoDTO addCarDetailPartsInfoDTO){
        BaseResponse baseResponse = new BaseResponse();
        try{
            for(int i=0;i<addCarDetailPartsInfoDTO.getCarPartsIds().length;i++){
                CarPartsInfoTypeDetailEntity carPartsInfoTypeDetailEntity = new CarPartsInfoTypeDetailEntity();
                carPartsInfoTypeDetailEntity.setCarPartsInfoId(addCarDetailPartsInfoDTO.getCarPartsIds()[i]);
                carPartsInfoTypeDetailEntity.setCarTypeDetailId(addCarDetailPartsInfoDTO.getCarTypeDetailId());
                carPartsInfoTypeDetailEntity.setCarTypeId(addCarDetailPartsInfoDTO.getCarTypeId());
                int flag = carTypeDetailService.addCarPartsInfo(carPartsInfoTypeDetailEntity);
                if(flag>0){
                    baseResponse.setData("保存成功!");
                }else{
                    baseResponse.setData("保存失败!");
                }
            }
        }catch (Exception e){
            log.info("保存车型信息异常!");
            baseResponse.setData("保存车型信息异常!");
        }
        return baseResponse;
    }
}
