package com.mmall.service;

import com.mmall.common.ServerResponse;

public interface ICategoryService {
    /**
     * 添加品类业务逻辑实现
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName, Integer parentId);
}
