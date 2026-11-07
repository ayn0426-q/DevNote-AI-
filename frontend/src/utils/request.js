//定制请求的实例

//导入axios  npm install axios
import {ElMessage} from 'element-plus'
import axios from 'axios';
//定义一个变量,记录公共的前缀  ,  baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({baseURL})
import { usetokenStore } from '@/stores/token.js'
instance.interceptors.request.use(
    config=>{
        //获取token
        const userTokenStore = usetokenStore();
        //如果token存在,就把token放到请求头中
        if(userTokenStore.token){
            config.headers.Authorization = userTokenStore.token;
        }
        return config;
    },
    (err)=>{
        return Promise.reject(err);
    }
)
import router from '@/router/index.js'
//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        console.log('后端返回完整数据:', result.data);
        if(result.data.code===0){
return result.data;
        }
        ElMessage.error(result.data.msg || '服务异常');
        return Promise.reject(new Error(result.data.msg));//异步的状态转化成失败的状态
    },
    err=>{
        if(err.response.status===401){
            ElMessage.error('请先登录');
            router.push('/login');
        }else {
            ElMessage.error(err.response.data.msg || '服务异常');
        }
        
        return Promise.reject(err);
    }
)

export default instance;