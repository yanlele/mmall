package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;

public interface IProductService {

    /**
     * 保存商品的业务逻辑
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(Product product);
}
