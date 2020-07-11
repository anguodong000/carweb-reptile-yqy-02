package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

@Mapper
public interface UserMapper {

    @Select("SELECT u.id as id,u.user_name as username,u.user_password as password,u.telephone,u.email FROM user_info u where u.telephone=#{telephone} limit 1")
    UserEntity findByUsername(String telephone);

    @Select("SELECT p.permission_code FROM user_info u " +
            "LEFT JOIN user_permission_info i " +
            "ON u.id=i.user_id " +
            "LEFT JOIN user_permission p " +
            "ON i.permission_id=p.id " +
            "WHERE u.id=#{userId}")
    Set<String> findPermissionsByUserId(int userId);

    @Select("SELECT COUNT(1) FROM user_info u WHERE u.telephone=#{telephone} ")
    int isUsernameRepeat(RegisterDto registerDto);

    @Insert("insert into user_info(user_name,user_password,telephone,email,company,company_address) values(#{username},#{password},#{telephone},#{email},#{company},#{companyAddress})")
    int createUser(UserEntity user);

    @Select("SELECT MAX(id) FROM user_info")
    int getMaxUserId();

    @Insert("insert into user_permission_info(user_id,permission_id) values(#{id},1)")
    int createPermission(UserEntity user);
}
