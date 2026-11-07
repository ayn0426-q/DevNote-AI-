import request from '@/utils/request.js'

export const userRegisterService = (registerData)=>{
    const param=new URLSearchParams()
    for(let key in registerData){
        param.append(key,registerData[key])
    }
   return request.post('/user/register',param);
}

export const userLoginService = (loginData)=>{
    const param=new URLSearchParams()
    for(let key in loginData){
        param.append(key,loginData[key])
    }
   return request.post('/user/login',param)
}

export const userInfoService= ()=>{
   return request.get('/user/userInfo')
}

export const updateuserInfoService=(userInfodata) =>{
    return request.put('/user/update',userInfodata)
}

export const  userAvatarUpdetaService=(avatarUrl)=>{
  return request.patch('/user/updateAvatar',null,{
    params :{
        avatarUrl:avatarUrl
    }
  })
}