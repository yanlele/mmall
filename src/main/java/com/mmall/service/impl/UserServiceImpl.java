package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;


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
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse<String> register(User user) {
        // 校验用户明是否存在
        int resultCount = userMapper.checkUsername(user.getUsername());
        if(resultCount > 0 ) {
            return ServerResponse.createByErrorMessage("用户名存在");
        }
        // 校验邮箱
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经存在");
        }

        // 赋予角色等级
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // DM5 加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        //　插入数据库
        resultCount = userMapper.insert(user);

        if (resultCount ==  0) {
            // 插入数据库失败的情况
            return ServerResponse.createByErrorMessage("用户注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }


}
