package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.entity.UserEntity;
import com.reptile.carwebreptileyqy.mapper.UserMapper;
import com.reptile.carwebreptileyqy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserEntity findByUsername(String telephone) {
        return userMapper.findByUsername(telephone);
    }

    @Override
    public Set<String> findPermissionsByUserId(int userId) {
        return userMapper.findPermissionsByUserId(userId);
    }

    @Override
    public int isUsernameRepeat(RegisterDto registerDto) {
        return userMapper.isUsernameRepeat(registerDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createUser(RegisterDto registerDto) {
        //获取用户最大id
        int userId = userMapper.getMaxUserId();
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setTelephone(registerDto.getTelephone());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setCompany(registerDto.getCompany());
        user.setCompanyAddress(registerDto.getCompanyAddress());
        user.setCreateTime(new Date());
        String passwordEncode = BCrypt.hashpw(registerDto.getPassword(),BCrypt.gensalt());
        user.setPassword(passwordEncode);
        userMapper.createUser(user);
        /**
         * 给用户赋权限
         */
        return userMapper.createPermission(user);
    }
}
