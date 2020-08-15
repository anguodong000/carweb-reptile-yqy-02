package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.CarPartsDTO;
import com.reptile.carwebreptileyqy.dto.QueryPriceDto;
import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.entity.CarPartsEntity;
import com.reptile.carwebreptileyqy.entity.UserEntity;
import com.reptile.carwebreptileyqy.service.CarPartsService;
import com.reptile.carwebreptileyqy.service.UserService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
    UserService userService;

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
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody CarPartsDTO carPartsDTO)  {
        BaseResponse baseResponse = new BaseResponse();
        Map responseMap = new LinkedHashMap();
        /**
         * 通过solrJ查询 7.1.0查询方式
         */
        List list = new ArrayList();
        long total = 0L;
        Map queryParamMap = new HashMap<>();
        if(carPartsDTO.getQueryString().equals("")){
            queryParamMap.put("q", "*:*");
        }else{
            queryParamMap.put("q", "textIk:"+carPartsDTO.getQueryString());
        }
        queryParamMap.put("start",carPartsDTO.getCurrentPage()-1);
        queryParamMap.put("rows",20);
        MapSolrParams queryParams = new MapSolrParams(queryParamMap);
        try{
            QueryResponse queryResponse = solrClient.query(queryParams);
            list = queryResponse.getResults();
            total = queryResponse.getResults().getNumFound();
            /**
             * 如果查询不出数据，就用编号去数据库查询（索引匹配问题，没有匹配到数据）
             */
            if(total==0){
                list = carPartsService.autoPartsInfoList(carPartsDTO);
                total = carPartsService.autoPartsInfoTotal(carPartsDTO);
            }
            responseMap.put("total",total);
            responseMap.put("currentPage",carPartsDTO.getCurrentPage());
            responseMap.put("autoPartsList",list);
            baseResponse.setData(responseMap);
        }catch(Exception e){
            log.info("查询solr结果异常,e=",e.getMessage());
        }
        return baseResponse;
    }

    /**
     * 查询价格新增
     * @param request
     * @param response
     * @param queryPriceDto
     * @return
     */
    @PostMapping(value = "/carParts/createPartsNeed",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('QUERY_PARTS_PRICE')")
    public BaseResponse createPartsNeed(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody QueryPriceDto queryPriceDto){
        String username = request.getRemoteUser();
        BaseResponse baseResponse = new BaseResponse();
        queryPriceDto.setUsername(username);
        //查询用户是否通过审核
        UserEntity user = userService.findByUsername(username);
        if(user.getIsAutyority()==0){
            baseResponse.setCode("401");
            baseResponse.setMessage("用户还未通过审核，请联系管理员！");
        }else{
            int i = carPartsService.createPriceNeed(queryPriceDto);
            if(i!=1){
                baseResponse.setCode("201");
                baseResponse.setMessage("error");
            }
        }
        return baseResponse;
    }

    /*@PostMapping(value = "/carWeb/register",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    //@PreAuthorize("hasAuthority('QUERY_PARTS_PRICE')")
    public BaseResponse register(
            @RequestBody RegisterDto registerDto){
        BaseResponse baseResponse = new BaseResponse();
        try{
            int result = userService.createUser(registerDto);
        }catch (Exception e){
            baseResponse.setCode("202");
            baseResponse.setExtraMessage("创建用户失败");
            log.info("创建用户异常："+e.getMessage());
        }
        return baseResponse;
    }*/
}
