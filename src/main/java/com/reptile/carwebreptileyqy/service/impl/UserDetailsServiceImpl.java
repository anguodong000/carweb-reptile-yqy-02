package com.reptile.carwebreptileyqy.service.impl;

import com.reptile.carwebreptileyqy.entity.UserEntity;
import org.springframework.security.core.userdetails.User;
import com.reptile.carwebreptileyqy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在");
        }
        //根据用户的id查询用户的权限
        Set<String> permissions = userService.findPermissionsByUserId(user.getId());
        //将permissions转成数组
        String[] permissionArray = new String[permissions.size()];
        permissions.toArray(permissionArray);
        UserDetails userDetails = User.withUsername(user.getTelephone()).password(user.getPassword()).authorities(permissionArray).build();
        return userDetails;
    }
}
