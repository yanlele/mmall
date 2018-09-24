package com.mmall.controller.protal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/*
 * @ResponseBody 这个是可以让我们的返回数据序列化为json
 */

@Controller
@RequestMapping("/user/")
public class UserController {

    @RequestMapping(value="login.do", method = RequestMethod.POST)
    @ResponseBody
    public Object login(String username, String password, HttpSession session) {
        // service -> mybatis -> dao
        return null;
    };
}
