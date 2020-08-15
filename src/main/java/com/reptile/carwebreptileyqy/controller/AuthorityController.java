package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.UserPermissionEntity;
import com.reptile.carwebreptileyqy.service.AuthorityService;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限相关
 */
@RestController
@Slf4j
public class AuthorityController {

    private static final String REGISTER_USER_AUTHORITY = "REGISTER_USER_AUTHORITY";

    @Autowired
    AuthorityService authorityService;

    /**
     * 查询是否具有审核权限页面
     * @param
     * @return
     */
    @PostMapping(value = "/authority/isHaveAuthorityPage",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse isHaveAuthorityPage(HttpServletRequest request, HttpServletResponse response){
        BaseResponse baseResponse = new BaseResponse();
        String username = request.getRemoteUser();
        List<UserPermissionEntity> userPermissionEntityList = authorityService.selectAuthorityByUsername(username);
        //存放权限名称
        Set<String> permissionNameSet = new HashSet<>();
        for(UserPermissionEntity userPermissionEntity : userPermissionEntityList){
            permissionNameSet.add(userPermissionEntity.getPermissionCode());
        }
        //如果包含直接返回，否则返回401
        if(permissionNameSet.contains(REGISTER_USER_AUTHORITY)){
        }else{
            baseResponse.setCode("401");
            baseResponse.setMessage("error");
        }
        return baseResponse;
    }

    /**
     * 查询是否具有审核权限页面
     * @param
     * @return
     */
    @PostMapping(value = "/authority/userAuthority",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse userAuthority(HttpServletRequest request, HttpServletResponse response,
            @RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        int r = authorityService.updateUserAuthority(userDTO);
        if(r>0){
        }else{
            baseResponse.setCode("201");
            baseResponse.setMessage("error");
        }
        return baseResponse;
    }
}
