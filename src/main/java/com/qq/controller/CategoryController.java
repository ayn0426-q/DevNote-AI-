package com.qq.controller;


import com.qq.pojo.Category;
import com.qq.pojo.Result;
import com.qq.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Result add(@RequestBody @Validated Category category){
        categoryService.add(category);
        return Result.success();
    }


    @GetMapping
    public Result<List<Category>> list(){
        List<Category> cs=categoryService.list();
        return Result.success(cs);
    }

    @GetMapping("/detail")
    public Result<Category> detail(Integer id){
        Category c=categoryService.findByid(id);
        return  Result.success(c);
    }

    @PutMapping
    public  Result update(@RequestBody @Validated Category category){
        categoryService.update(category);
        return Result.success(category);
    }

    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        categoryService.deleteByid(id);
        return Result.success();
    }
}
