package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.AddCarDetailPartsInfoDTO;
import com.reptile.carwebreptileyqy.dto.CarTypeDTO;
import com.reptile.carwebreptileyqy.entity.CarTypeEntity;
import com.reptile.carwebreptileyqy.service.CarTypeService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
public class CarTypeController {

    @Resource
    CarTypeService carTypeService;

    @RequestMapping("/carType/listHtml")
    public String list(ModelMap modelMap) {
        return "cartype/list";
    }

    @RequestMapping("/carType/addHtml")
    public String add(ModelMap modelMap) {
        return "cartype/add";
    }

    @RequestMapping("/carType/queryCarPartsListHtml")
    public String queryCarPartsList(ModelMap modelMap) {
        return "cartype/queryCarPartsList";
    }

    @RequestMapping("/carType/queryCarPartsByDetailIdHtml")
    public ModelAndView queryCarPartsByDetailId(
            @RequestParam(value="carTypeId",required=false) String carTypeId,
            @RequestParam(value="carTypeDetailId",required=false) String carTypeDetailId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cartype/query_carparts_bydetailId");
        modelAndView.addObject("carTypeId",carTypeId);
        modelAndView.addObject("carTypeDetailId",carTypeDetailId);
        return modelAndView;
    }

    /**
     * 车型列表查询
     * @param carTypeDTO
     * @return
     */
    @PostMapping(value = "/carType/list",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse listAll(
            @RequestBody CarTypeDTO carTypeDTO){
        BaseResponse baseResponse = new BaseResponse();
        Map map = new LinkedHashMap();
        List<CarTypeEntity> list = carTypeService.list(carTypeDTO);
        int total = carTypeService.total(carTypeDTO);
        map.put("total",total);
        map.put("currentPage",carTypeDTO.getCurrentPage());
        map.put("carTypeList",list);
        baseResponse.setData(map);
        return baseResponse;
    }

    /**
     * 添加
     * @param carTypeEntity
     * @return
     */
    @PostMapping(value = "/carType/add",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse add(
            @RequestBody CarTypeEntity carTypeEntity){
        BaseResponse baseResponse = new BaseResponse();
        try{
            int flag = carTypeService.add(carTypeEntity);
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
}

