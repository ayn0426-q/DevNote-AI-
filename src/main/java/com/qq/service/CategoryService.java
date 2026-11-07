package com.qq.service;

import com.qq.pojo.Category;

import java.util.List;

public interface CategoryService {
    void add(Category category);

    List<Category> list();

    Category findByid(Integer id);

    void update(Category category);

    void deleteByid(Integer id);
}
