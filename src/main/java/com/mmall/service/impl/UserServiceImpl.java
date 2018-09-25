package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 登录的实现
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        // 获取用户明是否存在
        int resultCount = userMapper.checkUsername(username);
        if(resultCount ==0 ) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if(user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        // 返回的时候，把密码设置为空
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册的具体实现
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        // 校验用户明是否存在
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }
        // 校验邮箱
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if(!validResponse.isSuccess()) {
            return validResponse;
        }

        // 赋予角色等级
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // DM5 加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        //　插入数据库
        int resultCount = userMapper.insert(user);

        if (resultCount ==  0) {
            // 插入数据库失败的情况
            return ServerResponse.createByErrorMessage("用户注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 实时校验接口实现
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if(StringUtils.isNotBlank(type)) {
            // 检验
            if(Const.USERNAME.equals(type)) {
                // 校验用户明是否存在
                int resultCount = userMapper.checkUsername(str);
                if(resultCount > 0 ) {
                    return ServerResponse.createByErrorMessage("用户名存在");
                }
            }
            if(Const.EMAIL.equals(type)) {
                // 校验邮箱
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已经存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createByErrorMessage("校验成功");
    }


    /**
     * 找回密码提示问题的具体实现
     * @param username
     * @return
     */
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }


    /**
     * 查询问题答案知否正确
     * @param username
     * @param question
     * @param answer
     * @return
     */
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username, question, answer);
        if (resultCount > 0) {
            // 如果问题答案正确，创建UUID
            String forgetToken = UUID.randomUUID().toString();
            // UUID 写入本地缓存，同时返回给客户端，作为更换密码的唯一票证
            TokenCache.setKey("token_"+username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }
}
