package com.qq.service.impl;

import com.qq.exception.BusinessException;
import com.qq.mapper.KnowledgeBaseServiceMapper;
import com.qq.pojo.KnowledgeBase;
import com.qq.pojo.dto.KnowledgeBaseDTO;
import com.qq.service.KnowledgeBaseService;

import com.qq.utils.ThreadLocalUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {
    @Autowired
    KnowledgeBaseServiceMapper knowledgeBaseServiceMapper;
    @Override
    public void addBase(KnowledgeBaseDTO knowledgeBaseDTO) {
        if (knowledgeBaseDTO.getName() == null ||
                knowledgeBaseDTO.getName().trim().isEmpty()) {
            throw new BusinessException("知识库名称不能为空");
        }
        Map<String,Object> claims= ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");
        Integer count = knowledgeBaseServiceMapper.testbase(
                knowledgeBaseDTO.getName(), userId
        );
        if (count!=0){
            throw new BusinessException("已创建同名知识库，不可重复创建");
        }
          KnowledgeBase knowledgeBase=new KnowledgeBase();
        knowledgeBase.setName(knowledgeBaseDTO.getName());
        knowledgeBase.setDescription(knowledgeBaseDTO.getDescription());
        knowledgeBase.setCreateUser(userId);
        knowledgeBaseServiceMapper.addBase(knowledgeBase);
    }

    @Override
    public List<KnowledgeBase> list() {
        Map<String,Object> claims= ThreadLocalUtil.get();
        Integer userId=(Integer) claims.get("id");
        List<KnowledgeBase> list=knowledgeBaseServiceMapper.list(userId);
        return list;
    }

    @Override
    public void update(Integer id, KnowledgeBase knowledgeBase) {

        // 1. 校验知识库名称
        if (knowledgeBase.getName() == null ||
                knowledgeBase.getName().trim().isEmpty()) {
            throw new BusinessException("知识库名称不能为空");
        }

        // 2. 根据 ID 查询原知识库
        KnowledgeBase oldBase = knowledgeBaseServiceMapper.findById(id);

        if (oldBase == null) {
            throw new BusinessException("知识库不存在");
        }

        // 3. 获取当前登录用户 ID
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        // 4. 校验知识库归属
        if (!userId.equals(oldBase.getCreateUser())) {
            throw new BusinessException("无权修改");
        }

        // 5. 判断名称是否发生变化，如果变化则检查重名
        if (!oldBase.getName().equals(knowledgeBase.getName())) {

            Integer count = knowledgeBaseServiceMapper.testUpdateName(
                    knowledgeBase.getName(), userId, id
            );
            if (count > 0) {
                throw new BusinessException("知识库名称重复");
            }
        }

        // 6. 设置需要更新的字段
        knowledgeBase.setId(id);
        knowledgeBase.setCreateUser(userId);

        // 7. 执行更新
        knowledgeBaseServiceMapper.updatebase(knowledgeBase);
    }

    @Override
    public void deleteBase(Integer id) {
        KnowledgeBase oldBase = knowledgeBaseServiceMapper.findById(id);
        if (oldBase==null){
            throw new BusinessException("知识库不存在");
        }
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");
        if (!userId.equals(oldBase.getCreateUser())) {
            throw new BusinessException("无权删除");
        }
        knowledgeBaseServiceMapper.deleteBase(id,userId);
    }

    @Override
    public KnowledgeBase findId(Integer id) {
        // 1. 根据 ID 查询知识库
        KnowledgeBase oldBase = knowledgeBaseServiceMapper.findById(id);

        // 2. 判断是否存在
        if (oldBase == null) {
            throw new BusinessException("知识库不存在");
        }

        // 3. 获取当前登录用户
        Map<String, Object> claims = ThreadLocalUtil.get();
        Integer userId = (Integer) claims.get("id");

        // 4. 校验知识库归属
        if (!userId.equals(oldBase.getCreateUser())) {
            throw new BusinessException("无权查看知识库");
        }

        // 5. 返回查询结果
        return oldBase;
    }
}
