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
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {
        // 获取用户明是否存在
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }

        // 返回的时候，把密码设置为空
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登录成功", user);
    }

    /**
     * 注册的具体实现
     *
     * @param user
     * @return
     */
    @Override
    public ServerResponse register(User user) {
        // 校验用户明是否存在
        ServerResponse validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        // 校验邮箱
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }

        // 赋予角色等级
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // DM5 加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        //　插入数据库
        int resultCount = userMapper.insert(user);

        if (resultCount == 0) {
            // 插入数据库失败的情况
            return ServerResponse.createByErrorMessage("用户注册失败");
        }
        return ServerResponse.createBySuccessMessage("注册成功");
    }

    /**
     * 实时校验接口实现
     * 这个是校验不存在的接口 如果返回isSuccess() == ture 说明这个是不存在的。
     *
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {
            // 检验
            if (Const.USERNAME.equals(type)) {
                // 校验用户明是否存在
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("用户名存在");
                }
            }
            if (Const.EMAIL.equals(type)) {
                // 校验邮箱
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ServerResponse.createByErrorMessage("邮箱已经存在");
                }
            }
        } else {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySuccessMessage("校验成功");
    }


    /**
     * 找回密码提示问题的具体实现
     *
     * @param username
     * @return
     */
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            // 用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.createBySuccess("成功返回你的问题", question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }


    /**
     * 查询问题答案知否正确
     *
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
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }


    /**
     * 忘记密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        // 校验token是否存在
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        // 校验用户名是否存在
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {     // 如果成功就是用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        // 拿到Token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        // 验证token 是否存在
        if(StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Token无效或者过期");
        }

        if(StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount > 0) {
                return ServerResponse.createBySuccessMessage("修改密码成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("Token错误， 请重新获取重置密码的Token");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }


    /**
     * 登录状态下修改密码的实现
     * @param passwordOld
     * @param passwordNew
     * @param user
     * @return
     */
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权,要校验一下这个用户的旧密码,一定要指定是这个用户.因为我们会查询一个count(1),如果不指定id,那么结果就是true啦count>0;
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(resultCount == 0) {
            // 说明久密码错误
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");
    }


    /**
     * 更新用户信息
     * @param user
     * @return
     */
    public ServerResponse<User> updateInformation(User user) {
        // username 不能被更新，email不能相同
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(resultCount> 0) {
            return ServerResponse.createByErrorMessage("邮箱已经存在");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount> 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", updateUser);
        }

        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user==null) {
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        // 找到用户之后，但是不能把密码给用户
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("查询用户信息成功", user);
    }
}
