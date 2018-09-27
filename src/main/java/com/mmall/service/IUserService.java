package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * IUserService interface
 * @author yanlele
 * @data 2018/09/25
 */

public interface IUserService {

    /**
     * 登录接口
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 注册接口
     * @param user
     * @return
     */
    public ServerResponse<String> register(User user);


    /**
     * 实时校验接口
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type);


    /**
     * 找回密码提示问题的具体实现
     * @param username
     * @return
     */
    public ServerResponse<String> selectQuestion(String username);


    /**
     * 查询问题答案知否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer);


    /**
     * 忘记密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken);


    /**
     * 登录状态下修改密码的实现
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);


    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateInformation(User user);


    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getInformation(Integer userId);
}
