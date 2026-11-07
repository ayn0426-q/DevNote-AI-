import {createRouter, createWebHashHistory} from 'vue-router'
import Login from '@/views/login.vue'
import Layout from '@/views/Layout.vue'
import ArticleCategory from '@/views/article/ArticleCategory.vue'
import ArticleManageVue from '@/views/article/ArticleManage.vue'
import UserAvatarVue  from '@/views/user/UserAvatar.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'


const routes=[
    {path:'/login',component: Login},
    {
        path:'/',
        component:Layout,redirect:'/article-manage',
        children:[
            {path:'article-category',component:ArticleCategory},
            {path:'article-manage',component:ArticleManageVue},
            {path:'user-avatar',component:UserAvatarVue},
            {path:'user-info',component:UserInfoVue},
            {path:'user-reset-password',component:UserResetPasswordVue}
        ]
    }
]

const router=createRouter({
    history:createWebHashHistory(),
    routes:routes
})


export default router