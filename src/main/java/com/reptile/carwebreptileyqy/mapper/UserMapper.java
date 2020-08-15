package com.reptile.carwebreptileyqy.mapper;

import com.reptile.carwebreptileyqy.dto.RegisterDto;
import com.reptile.carwebreptileyqy.dto.UserDTO;
import com.reptile.carwebreptileyqy.entity.UserEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {

    @Select("SELECT u.id as id,u.user_name as username,u.user_password as password,u.telephone,u.email,u.company,u.company_address companyAddress,u.is_authority isAutyority FROM user_info u where u.telephone=#{telephone} limit 1")
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

    @Insert("insert into user_info(id,user_name,user_password,telephone,email,company,company_address,create_time,is_authority) values(#{id},#{username},#{password},#{telephone},#{email},#{company},#{companyAddress},#{createTime},0)")
    int createUser(UserEntity user);

    @Select("SELECT MAX(id)+1 FROM user_info")
    int getMaxUserId();

    @Insert("insert into user_permission_info(user_id,permission_id,create_time) values(#{id},1,#{createTime})")
    int createPermission(UserEntity user);

    @Select("SELECT u.user_name username,u.telephone,u.email,u.company,u.company_address companyAddress,u.is_authority isAutyority FROM user_info u order by create_time desc")
    List<UserEntity> selectAllUser(UserDTO userDTO);

    @Select("SELECT COUNT(1) FROM user_info u ")
    int selectCount(UserDTO userDTO);
}
