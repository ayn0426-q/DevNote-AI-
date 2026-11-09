package com.qq.controller;


import com.qq.pojo.KnowledgeBase;
import com.qq.pojo.PageBean;
import com.qq.pojo.Result;
import com.qq.pojo.dto.KnowledgeBaseDTO;
import com.qq.service.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/knowledge-base")
public class KnowledgeBaseController {
    @Autowired
    KnowledgeBaseService knowledgeBaseService;


    @PostMapping()
    public Result addBase(@RequestBody KnowledgeBaseDTO knowledgeBaseDTO){
        knowledgeBaseService.addBase(knowledgeBaseDTO);
        return Result.success("添加成功");
    }


    @GetMapping()
    public Result<List<KnowledgeBase>> List( ){
      List<KnowledgeBase> list=knowledgeBaseService.list();
        return Result.success(list);
    }
     @PutMapping("/{id}")
    public Result update(@PathVariable("id") Integer id,@RequestBody KnowledgeBase knowledgeBase ){
        knowledgeBaseService.update(id,knowledgeBase);
        return Result.success();
     }

    @GetMapping("/{id}")
    public Result<KnowledgeBase> findId(@PathVariable("id") Integer id) {
        KnowledgeBase knowledgeBase = knowledgeBaseService.findId(id);
        return Result.success(knowledgeBase);
    }

     @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Integer id){
        knowledgeBaseService.deleteBase(id);
        return Result.success();
     }
}
