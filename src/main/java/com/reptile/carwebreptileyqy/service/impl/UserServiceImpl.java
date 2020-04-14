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

import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserEntity findByUsername(String username) {
        return userMapper.findByUsername(username);
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
        UserEntity user = new UserEntity();
        user.setUsername(registerDto.getUsername());
        user.setTelephone(registerDto.getTelephone());
        user.setEmail(registerDto.getEmail());
        String passwordEncode = BCrypt.hashpw(registerDto.getPassword(),BCrypt.gensalt());
        user.setPassword(passwordEncode);
        /**
         * 获取用户最大的id
         */
        int userId = userMapper.getMaxUserId();
        user.setId(userId+1);
        userMapper.createUser(user);
        /**
         * 给用户赋权限
         */
        return userMapper.createPermission(user);
    }
}
