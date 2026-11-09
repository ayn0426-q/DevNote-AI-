package com.qq.service;

import com.qq.pojo.KnowledgeBase;
import com.qq.pojo.dto.KnowledgeBaseDTO;

import java.util.List;

public interface KnowledgeBaseService {
    void addBase(KnowledgeBaseDTO knowledgeBaseDTO);

    List<KnowledgeBase> list();

    void update(Integer id, KnowledgeBase knowledgeBase);

    void deleteBase(Integer id);

    KnowledgeBase findId(Integer id);
}
