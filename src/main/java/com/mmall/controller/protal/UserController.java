package com.mmall.controller.protal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
 * <p>
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
     *
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = iUserService.login(username, password);
        if (response.isSuccess()) {
            // 存入session
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    ;

    /**
     * 登出接口
     *
     * @param session
     * @return
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }

    /**
     * 实时判断用户名和邮箱
     *
     * @param str
     * @param type
     * @return
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户没有登录，无法获取用户信息");
    }

    /**
     * 找回面获取问题
     * @param username
     * @return
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }

    /**
     * 检测问题是否正确，如果正确的话，拿到唯一票证，作为修改密码的时候用
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 传入用户名，密码，token修改密码: 重置密码
     * 这个是未登陆状态下的重置密码
     * @param username
     * @param passwordNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetRestPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetRestPassword(username, passwordNew, forgetToken);
    }


    /**
     * 登录状态下重置密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null) {
            return ServerResponse.createByErrorMessage("用户没有登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }


    /**
     * 更新用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        // 查看用户是否登录
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null) {
            return ServerResponse.createByErrorMessage("用户没有登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());

        // 更新用户操作
        ServerResponse<User> response = iUserService.updateInformation(user);

        // 更新缓存信息
        if(response.isSuccess()) {
            // 不能更新用户名
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 查询用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInformation(HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "没有登录，需要强制登录");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}
