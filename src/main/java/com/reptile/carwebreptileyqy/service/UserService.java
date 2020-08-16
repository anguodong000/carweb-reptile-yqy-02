package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.UserEntity;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    Set<String> findPermissionsByUserId(int userId);

    int isUsernameRepeat(RegisterDto registerDto);

    int createUser(RegisterDto registerDto);

    List<UserEntity> list(UserDTO userDTO);

    int userTotal(UserDTO userDTO);

    int updateUser(UserEntity user);
}
