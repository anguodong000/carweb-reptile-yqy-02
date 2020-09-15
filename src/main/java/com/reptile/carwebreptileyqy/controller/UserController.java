package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.UserEntity;
import com.reptile.carwebreptileyqy.service.UserService;
import com.reptile.carwebreptileyqy.service.impl.UserDetailsServiceImpl;
import com.reptile.carwebreptileyqy.util.BaseResponse;
import com.reptile.carwebreptileyqy.util.MD5Util;
import com.reptile.carwebreptileyqy.util.SendMail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.util.*;

@RestController
@Slf4j
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        List<UserEntity> listDTO = new ArrayList<>();
        for(UserEntity user:list){
            user.setEmail(user.getEmail() == null ? "" : user.getEmail());
            user.setCompany(user.getCompany() == null? "":user.getCompany());
            user.setCompanyAddress(user.getCompanyAddress() == null? "":user.getCompanyAddress());
            if(!"YQY".equals(user.getUsername()) && !"anguodong".equals(user.getUsername()) && !"13439970960".equals(user.getUsername()) ){
                listDTO.add(user);
            }
        }
        int total = userService.userTotal(userDTO);
        map.put("total",total);
        map.put("currentPage",userDTO.getCurrentPage());
        map.put("userList",listDTO);
        baseResponse.setData(map);
        return baseResponse;
    }

    @PostMapping(value = "/user/forgetPassword",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse forgetPassword(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        UserEntity user = userService.findByEmail(userDTO.getEmail());
        if(user == null){
            baseResponse.setCode("4003");
            baseResponse.setMessage("用户不存在,请输入注册时邮箱地址");
        }else{
            //密钥
            String secretKey= UUID.randomUUID().toString();
            //30分钟后过期
            Timestamp outDate = new Timestamp(System.currentTimeMillis()+30*60*1000);
            // 忽略毫秒数
            long date = outDate.getTime() / 1000 * 1000;

            user.setValidateCode(secretKey);
            user.setOutDate(outDate);
            userService.updateUser(user);

            String key = user.getTelephone()+"$"+date+"$"+secretKey;
            //数字签名
            String digitalSignature = MD5Util.encode(key);

            String emailTitle = "配齐网密码找回";
            String path = request.getContextPath();
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
            String resetPassHref = basePath+"user/reset_password?sid="+digitalSignature+"&username="+user.getTelephone();
            String emailContent = "请勿回复本邮件.点击下面的链接,重设密码<br/><a href="+resetPassHref +" rel='external nofollow' target='_BLANK'>点击我重新设置密码</a>" +
                    "<br/>tips:本邮件超过30分钟,链接将会失效，需要重新申请'找回密码'"+key+"\t"+digitalSignature;
            SendMail.sendMail(user.getEmail(),emailContent);
        }
        return baseResponse;
    }

    @PostMapping(value = "/user/updatePassword",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse updatePassword(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        //查询用户
        UserEntity userDetail = userService.findByUsername(userDTO.getTelephone());
        if(userDetail.getValidateCode()==null || !userDetail.getValidateCode().equals(userDTO.getValidateCode())){
            baseResponse.setCode("203");
            baseResponse.setMessage("用户匹配错误!");
        }
        UserEntity user = new UserEntity();
        user.setTelephone(userDTO.getTelephone());
        String passwordEncode = BCrypt.hashpw(userDTO.getPassword(),BCrypt.gensalt());
        user.setPassword(passwordEncode);
        int r=userService.updateUserPassword(user);
        if(r!=1){
            baseResponse.setCode("202");
            baseResponse.setMessage("修改密码失败");
            return baseResponse;
        }
        return baseResponse;
    }
}
