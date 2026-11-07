import request from '@/utils/request.js'
import {usetokenStore} from '@/stores/token.js'
export const getArticleListService = ()=>{
    const tokenStore = usetokenStore();
     // 获取 token
    // return request.get('/category',
    //     {headers:{Authorization:tokenStore.token}});
    return request.get('/category');
}

export const addArticleCategoryService = (data)=>{
    return request.post('/category',data);
}

export const updateArticleCategoryService = (data)=>{
    return request.put('/category',data);
}

export const deleteArticleCategoryService = (id)=>{
    return request.delete(`/category`,{
        params:{id:id}
    });
}

export const articleListService = (params)=>{
    return request.get('/article', {params:params});
}



export const addArticleService = (data)=>{
    return request.post('/article',data);
}