package com.reptile.carwebreptileyqy.service;

import com.reptile.carwebreptileyqy.entity.UserPermissionEntity;

import java.util.List;

public interface AuthorityService {

    List<UserPermissionEntity> selectAuthorityByUsername(String username);
}
