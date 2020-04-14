package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.entity.UserEntity;

import java.util.Set;

public interface UserService {
    UserEntity findByUsername(String username);

    Set<String> findPermissionsByUserId(int userId);

    int isUsernameRepeat(RegisterDto registerDto);

    int createUser(RegisterDto registerDto);
}
