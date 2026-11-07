package com.qq.service;

import com.qq.pojo.Tag;

import java.util.List;

public interface TagService {
    void add(Tag tag);

    List<Tag> findTag();

    void fixTag(Tag tag);

    void deleteTag(Integer id);
}
