package com.reptile.carwebreptileyqy.controller;

import com.reptile.carwebreptileyqy.dto.LoginDTO;
import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.service.UserService;
import com.reptile.carwebreptileyqy.service.impl.UserDetailsServiceImpl;
import com.reptile.carwebreptileyqy.util.BaseResponse;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping(value = "/carWeb/login",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse login1(@RequestBody LoginDTO loginDTO, HttpServletRequest request){
        BaseResponse baseResponse = new BaseResponse();
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        // 系统登录认证
        //JwtAuthenticatioToken token = SecurityUtils.login(request, username, password, authenticationManager);
        return baseResponse;
    }

    /**
     * 用户注册
     * @param registerDto
     * @return
     */
    @PostMapping(value = "/carWeb/register",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse register(
            @RequestBody RegisterDto registerDto,HttpServletRequest request){
        BaseResponse baseResponse = new BaseResponse();
        try{
            int userCount = userService.isUsernameRepeat(registerDto);
            if(userCount!=0){
                baseResponse.setCode("201");
                baseResponse.setExtraMessage("手机号已存在");
            }else{
                userService.createUser(registerDto);
                UserDetails userDetails = userDetailsService.loadUserByUsername(registerDto.getTelephone());
                //进行授权登录
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(registerDto.getTelephone(), registerDto.getPassword(),userDetails.getAuthorities());
                try{
                    token.setDetails(new WebAuthenticationDetails(request));
                    Authentication authenticatedUser = authenticationManager.authenticate(token);
                    SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
                    request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
                }catch (Exception e){
                    log.info("用户授权失败："+e.getMessage());
                }
            }
        }catch (Exception e){
            baseResponse.setCode("202");
            baseResponse.setExtraMessage("创建用户失败");
            //userService.deleteUserById();
            log.info("创建用户异常："+e.getMessage());
        }
        return baseResponse;
    }

    /**
     * 判断用户名是否重复
     * @param registerDto
     * @param request
     * @return
     */
    @PostMapping(value = "/carWeb/isUsernameRepeat",produces = MediaType.APPLICATION_JSON)
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse isUsernameRepeat(@RequestBody RegisterDto registerDto, HttpServletRequest request){
        BaseResponse baseResponse = new BaseResponse();
        int userCount = userService.isUsernameRepeat(registerDto);
        if(userCount!=0){
            baseResponse.setCode("201");
            baseResponse.setExtraMessage("用户已存在");
        }
        return baseResponse;
    }
}
