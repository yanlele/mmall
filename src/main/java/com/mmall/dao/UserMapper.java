package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // 校验用户是否存在
    int checkUsername(String username);

    // 检查用户名和密码对不对
    // 多个参数注入的时候，要用@param注解
    User selectLogin(@Param("username") String username, @Param("password") String password);

    // 校验用户邮箱是否存在
    int checkEmail(String email);

    // 查询用户提示问题
    String selectQuestionByUsername(String username);

    // 检查用户提示问题的回答答案是否正确
    int checkAnswer(@Param("username")String username, @Param("question")String question, @Param("answer")String answer);

    // 更细用户密码
    int updatePasswordByUsername(@Param("username")String username,@Param("passwordNew") String passwordNew);
}