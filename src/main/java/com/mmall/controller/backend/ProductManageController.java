package com.mmall.controller.backend;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;


    /**
     * 保存商品
      * @param session
     * @param product
     * @return
     */
    @RequestMapping(value = "save.do", method= RequestMethod.POST)
    @ResponseBody
    public ServerResponse productSave(HttpSession session, Product product) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse checkResult = checkFunction(currentUser);
        if (!checkResult.isSuccess()) {
            return checkResult;
        }
        // 业务逻辑部分
        return iProductService.saveOrUpdateProduct(product);
    }



    /**
     * 校验是否登录 是否是管理员
     * @param user
     * @return
     */
    private ServerResponse checkFunction(User user) {
        if(user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户没有登录，请先登录");
        }
        // 检验是否是管理员
        if(!iUserService.checkAdmin(user).isSuccess()) {
            return ServerResponse.createByErrorMessage("不是管理员，没有权限操作");
        }
        return ServerResponse.createBySuccessMessage("验证通过");
    }
}
