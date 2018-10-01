package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    /**
     * 保存商品的业务逻辑
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(Product product) {
        if(product!=null) {
            if(StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if(product.getId()!=null) {
                // 有ID ，是更新的逻辑部分
                int rowCount = productMapper.updateByPrimaryKeySelective(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新商品成功");
                }
                return ServerResponse.createByErrorMessage("更新商品失败");
            } else {
                int rowCount = productMapper.insertSelective(product);
                if(rowCount > 0) {
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }
                return ServerResponse.createByErrorMessage("新增商品失败");
            }
        }
        return ServerResponse.createByErrorMessage("新郑或者更新产品参数不正确");
    }

    /**
     * 修改商品状态
     * @param productId
     * @param status
     * @return
     */
    public ServerResponse setSaleStatus(Integer productId, Integer status) {
        if(productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if(rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更改商品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("更改商品销售状态失败");
    }

    // 查询商品详情
    public ServerResponse<Product> manageProductDetail(Integer productId) {
        if(productId == null) {
            return ServerResponse.createByErrorMessage("没有传入商品ID");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null) {
            return ServerResponse.createByErrorMessage("商品已经下架或者已经被删除");
        }
        // todo 写一个productDetailVo
        return null;
    }
}
