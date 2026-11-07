package com.qq.service.impl;


import com.qq.exception.BusinessException;
import com.qq.mapper.TagMapper;
import com.qq.pojo.Result;
import com.qq.pojo.Tag;
import com.qq.service.TagService;
import com.qq.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class TagImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
    @Override
    public void add(Tag tag) {
        Map<String,Object> claims= ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");
        tag.setCreateUser(userId);
        Integer count=tagMapper.testRepeat(tag.getTagName(),userId);
        if (count>0){
            throw new BusinessException("该标签已存在，不可重复添加");
        }
        tagMapper.add(tag);
    }

    @Override
    public List<Tag> findTag() {
        Map<String,Object> clamis=ThreadLocalUtil.get();
        Integer userId=(Integer) clamis.get("id");
        return tagMapper.findTag(userId);
    }

    @Override
    public void fixTag(Tag tag) {
        Map<String,Object> clamis=ThreadLocalUtil.get();
        Integer userId=(Integer) clamis.get("id");
        Integer count=tagMapper.testid(tag.getId(),userId);
        if (count!=1){
            throw new  BusinessException("标签不存在或无权修改");
        }
        tagMapper.fixTag(tag.getId(), tag.getTagName(), userId);
    }

    @Override
    @Transactional
    public void deleteTag(Integer id) {
        Map<String,Object> clamis=ThreadLocalUtil.get();
        Integer userId=(Integer) clamis.get("id");
        Integer count=tagMapper.testid(id,userId);
        if (count!=1){
            throw new  BusinessException("标签不存在");
        }
        tagMapper.deleteabout(id);
        tagMapper.deleteTag(id,userId);
    }
}
