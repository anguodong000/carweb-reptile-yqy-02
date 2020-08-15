package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.CarHingesDTO;
import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.CarHingesInfoEntity;
import com.reptile.carwebreptileyqy.entity.UserEntity;
import com.reptile.carwebreptileyqy.service.UserService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 用户列表查询
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/user/list",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse list(
            @RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        Map map = new LinkedHashMap();
        List<UserEntity> list = userService.list(userDTO);
        for(UserEntity user:list){
            user.setEmail(user.getEmail() == null ? "" : user.getEmail());
            user.setCompany(user.getCompany() == null? "":user.getCompany());
            user.setCompanyAddress(user.getCompanyAddress() == null? "":user.getCompanyAddress());
            if(user.getIsAutyority()==0){
                user.setIsAutyorityName("新注册");
            }else{
                user.setIsAutyorityName("已审核");
            }
        }
        int total = userService.userTotal(userDTO);
        map.put("total",total);
        map.put("currentPage",userDTO.getCurrentPage());
        map.put("userList",list);
        baseResponse.setData(map);
        return baseResponse;
    }
}
