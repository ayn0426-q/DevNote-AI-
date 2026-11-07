package com.qq.service.impl;

import com.qq.mapper.CategoryMapper;
import com.qq.pojo.Category;
import com.qq.service.CategoryService;
import com.qq.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CategoryImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Override
    public void add(Category category) {
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer userId= (Integer) map.get("id");
        category.setCreateUser(userId);
        categoryMapper.add(category);
    }

    @Override
    public List<Category> list() {
        Map<String,Object> map=ThreadLocalUtil.get();
        Integer userid=(Integer) map.get("id");
        return categoryMapper.list(userid);
    }

    @Override
    public Category findByid(Integer id) {
        Category c=categoryMapper.finByid(id);
        return c;
    }

    @Override
    public void update(Category category) {
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.update(category);

    }

    @Override
    public void deleteByid(Integer id) {
        categoryMapper.delete(id);
    }
}
