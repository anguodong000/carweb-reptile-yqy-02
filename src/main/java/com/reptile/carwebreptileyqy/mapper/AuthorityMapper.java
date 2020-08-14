package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.entity.UserPermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface AuthorityMapper {

    @Select("select u.id userId,p.permission_name permissionName,p.permission_code permissionCode from user_info u " +
            "left join user_permission_info i " +
            "on u.id=i.user_id " +
            "left join user_permission p " +
            "on i.permission_id=p.id " +
            "where u.telephone = #{username}")
    List<UserPermissionEntity> selectAuthorityByUsername(String username);
}
