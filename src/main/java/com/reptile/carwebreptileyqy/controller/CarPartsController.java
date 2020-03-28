package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.CarPartsDTO;
import com.reptile.carwebreptileyqy.entity.AutoPartsInfoEntity;
import com.reptile.carwebreptileyqy.entity.CarPartsEntity;
import com.reptile.carwebreptileyqy.service.CarPartsService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.*;

@RestController
@Slf4j
public class CarPartsController {

    @Resource
    CarPartsService carPartsService;

    @Autowired
    SolrClient solrClient;

    @Autowired
    SolrTemplate solrTemplate;

    @PostMapping(value = "/carParts/upload")
    @ResponseBody
    public String uploadExcel(@RequestParam("file") MultipartFile file) {

        try {
            Object result = carPartsService.upload(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "上传失败!";
        }
        return "上传成功!";
    }

    @PostMapping(value = "/carParts/list",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse listAll(
            @RequestBody CarPartsDTO carPartsDTO){
        BaseResponse baseResponse = new BaseResponse();
        Map map = new LinkedHashMap();
        List<CarPartsEntity> list = carPartsService.list(carPartsDTO);
        int total = carPartsService.total(carPartsDTO);
        map.put("total",total);
        map.put("currentPage",carPartsDTO.getCurrentPage());
        map.put("carPartsList",list);
        baseResponse.setData(map);
        return baseResponse;
    }

    @PostMapping(value = "/carParts/queryCarPartsByDetailId",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse queryCarPartsByDetailId(
            @RequestBody CarPartsDTO carPartsDTO){
        BaseResponse baseResponse = new BaseResponse();
        Map map = new LinkedHashMap();
        List<CarPartsEntity> list = carPartsService.queryCarPartsByDetailId(carPartsDTO);
        int total = carPartsService.totalQueryCarPartsByDetailId(carPartsDTO);
        map.put("total",total);
        map.put("currentPage",carPartsDTO.getCurrentPage());
        map.put("carPartsByDetailIdList",list);
        baseResponse.setData(map);
        return baseResponse;
    }

    @PostMapping(value = "/autoPartsInfo/list",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse autoPartsInfoList(
            @RequestBody CarPartsDTO carPartsDTO)  {
        BaseResponse baseResponse = new BaseResponse();
        Map responseMap = new LinkedHashMap();
        /*List<AutoPartsInfoEntity> list = carPartsService.autoPartsInfoList(carPartsDTO);
        int total = carPartsService.autoPartsInfoTotal(carPartsDTO);*/

        /**
         * 通过solrJ查询 7.1.0查询方式
         */
        Map<String, String> queryParamMap = new HashMap<>();
        if(carPartsDTO.getQueryString().equals("")){
            queryParamMap.put("q", "*:*");
        }else{
            queryParamMap.put("q", "textIk:"+carPartsDTO.getQueryString());
        }
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        try{
            QueryResponse response = solrClient.query(queryParams);
            SolrDocumentList solrDocumentList = response.getResults();
            long total = response.getResults().getNumFound();
            responseMap.put("total",total);
            responseMap.put("currentPage",carPartsDTO.getCurrentPage());
            responseMap.put("autoPartsList",solrDocumentList);
            baseResponse.setData(responseMap);
        }catch(Exception e){
            log.info("查询solr结果异常,e=",e.getMessage());
        }
        return baseResponse;
    }
}
