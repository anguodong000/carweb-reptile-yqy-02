package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.UserPermissionEntity;

import java.util.List;

public interface AuthorityService {

    List<UserPermissionEntity> selectAuthorityByUsername(String username);

    int updateUserAuthority(UserDTO userDTO);
}
