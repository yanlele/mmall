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
}
