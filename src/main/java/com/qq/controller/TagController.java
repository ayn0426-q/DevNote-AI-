package com.qq.controller;

import com.qq.pojo.Result;
import com.qq.pojo.Tag;
import com.qq.service.TagService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")

public class TagController {
    @Autowired
    public TagService tagService;

    @PostMapping
    public Result addTag(@RequestBody Tag tag) {
        tagService.add(tag);
        return Result.success();
    }

    @GetMapping
    public Result<List<Tag>> list() {
        List<Tag> tg = tagService.findTag();
        return Result.success(tg);
    }


    @PutMapping
    public  Result fixTag(@RequestBody Tag tag){
     tagService.fixTag(tag);
     return  Result.success();
    }

    @DeleteMapping("/{id}")
    public  Result deleteTag(@PathVariable Integer id){
        tagService.deleteTag(id);
        return Result.success();
    }
}


