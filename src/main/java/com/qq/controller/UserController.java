package com.qq.controller;

import com.auth0.jwt.JWT;
import com.qq.pojo.Result;
import com.qq.pojo.User;
import com.qq.service.UserService;
import com.qq.utils.JwtUtil;
import com.qq.utils.Md5Util;
import com.qq.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.apache.el.parser.Token;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping("/register")
    public Result register(@Pattern(regexp ="^\\S{5,16}$") String username, @Pattern(regexp ="^\\S{5,16}$" ) String password){
        User u=userService.findByName(username);
        if (u==null){
           userService.register( username,password);
            return Result.success();
        }else {
            return Result.error("用户名重复请重新注册");
        }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp ="^\\S{5,16}$") String username, @Pattern(regexp ="^\\S{5,16}$" ) String password){
        User loginuser=userService.findByName(username);
        if (loginuser==null){
            return Result.error("用户名错误");
        }
        if (Md5Util.getMD5String(password).equals(loginuser.getPassword())){
            Map<String,Object> claims= new HashMap<>();
            claims.put("id",loginuser.getId());
            claims.put("username",loginuser.getUsername());
           String Token=JwtUtil.genToken(claims);
            ValueOperations<String,String>  operations=stringRedisTemplate.opsForValue();
            operations.set(Token,Token,1, TimeUnit.HOURS);
            return  Result.success(Token);
        }else {
            return Result.error("密码错误");
        }
    }

    @GetMapping("userInfo")
   public Result userInfo(/*@RequestHeader(name = "Authorization") String Token*/){
//        Map<String,Object> map=JwtUtil.parseToken(Token);
//        String username=(String) map.get("username");
    Map<String,Object> map=ThreadLocalUtil.get();
    String username=(String) map.get("username");
        User user=userService.findByName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result userupdate(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public  Result updateAvatar(@RequestParam @URL  String avatarUrl){
        userService.updateAvater(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params ,@RequestHeader("Authorization") String Token) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要参数");
        }
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginuser = userService.findByName(username);
        if (!loginuser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码填写不正确");
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次密码填写不一致");
        }
        userService.updatePwd(newPwd);
        ValueOperations<String,String> operations=stringRedisTemplate.opsForValue();
        operations.getOperations().delete(Token);
        return Result.success();
    }
}
