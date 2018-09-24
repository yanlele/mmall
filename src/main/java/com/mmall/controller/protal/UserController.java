package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * UserController class
 *
 * @author yanle
 * @date 2018/09/25
 *
 * 其他说明：
 * @ResponseBody 这个是可以让我们的返回数据序列化为json
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;


    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value="login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if(response.isSuccess()) {
            // 存入session
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    };

    /**
     * 登出接口
     * @param session
     * @return
     */
    @RequestMapping(value="logout.do", method=RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册用户
     * @param user
     * @return
     */
    @RequestMapping(value="register.do", method=RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 实时判断用户名和邮箱
     * @param str
     * @param type
     * @return
     */
    public ServerResponse<String> checkValid(String str, String type) {
        return null;
    }
}
