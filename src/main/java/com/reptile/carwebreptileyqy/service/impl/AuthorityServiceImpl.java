package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.UserPermissionEntity;
import com.reptile.carwebreptileyqy.mapper.AuthorityMapper;
import com.reptile.carwebreptileyqy.service.AuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    AuthorityMapper authorityMapper;

    @Override
    public List<UserPermissionEntity> selectAuthorityByUsername(String username) {
        List<UserPermissionEntity> userPermissionList = authorityMapper.selectAuthorityByUsername(username);
        return userPermissionList;
    }

    @Override
    public int updateUserAuthority(UserDTO userDTO) {
        userDTO.setUpdateTime(new Date());
        return authorityMapper.updateUserAuthority(userDTO);
    }
}
