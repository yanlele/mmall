package com.mmall.service.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.List;
import java.util.Set;

@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {
    // 定义日志
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 添加品类业务逻辑实现
     *
     * @param categoryName
     * @param parentId
     * @return
     */
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);       // 为 true 说明这个分类可以用

        int rowCount = categoryMapper.insert(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }


    /**
     * 更新种类名称
     *
     * @param categoryName
     * @param categoryId
     * @return
     */
    public ServerResponse updateCategoryName(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更新种类名称成功");
        }
        return ServerResponse.createByErrorMessage("更新种类名称失败");
    }


    /**
     * 获取当前节点的子元素
     *
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("没有找到当前分类的子类");
        }
        return ServerResponse.createBySuccess(categoryList);
    }


    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId) {
        // 建立一个空的set对象
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null) {
            for(Category categoryItem: categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }


    //递归算法,算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        // 搜索当前类
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList) {
            findChildCategory(categorySet, categoryItem.getId());
        }
        return categorySet;
    }
}
