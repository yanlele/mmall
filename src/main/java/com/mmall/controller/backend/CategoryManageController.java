package com.mmall.controller.backend;


import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;


    /**
     * 添加种类
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping(value = "add_category.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addCategory(HttpSession session, String categoryName, @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        // 校验是否登录 是否是管理员
        ServerResponse checkResult =  checkFunction(currentUser);
        if(!checkResult.isSuccess()) {
            return checkResult;
        }
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * 修改种类名称
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "set_category_name.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session, Integer categoryId, String categoryName) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse checkResult = checkFunction(currentUser);
        if(!checkResult.isSuccess()) {
            return checkResult;
        }
        // 检查是否是管理员
        return iCategoryService.updateCategoryName(categoryName, categoryId);
    }


    /**
     * 获取当前分类
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse checkResult = checkFunction(currentUser);
        if(!checkResult.isSuccess()) {
            return checkResult;
        }
        return iCategoryService.getChildrenParallelCategory(categoryId);
    }


    /**
     * 深度查询子分类的节点id
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,  @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        ServerResponse checkResult = checkFunction(currentUser);
        if(!checkResult.isSuccess()) {
            return checkResult;
        }
        return iCategoryService.selectCategoryAndChildrenById(categoryId);
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
