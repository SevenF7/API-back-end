package apiwork.controller;

import apiwork.pojo.Result;
import apiwork.pojo.User;
import apiwork.service.UserService;
import apiwork.utils.GenerateRandomCodeUtil;
import apiwork.utils.JwtUtil;
import apiwork.utils.Md5Util;
import apiwork.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private GenerateRandomCodeUtil generateRandomCodeUtil;
    //Redis相关内容，为了方便测试，最后一次版本取消注释即可
    @Autowired
    private StringRedisTemplate stringRedisTemplate1;
    @Autowired
    private StringRedisTemplate stringRedisTemplate2;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);



    // Done
    //注册
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, String nickname, String email,
                           @Pattern(regexp = "^\\S{5,16}$") String password, String rePwd, String code) {
        logger.info("Received registration request for username: {}", username);

        //查询用户
        User u = userService.findByUserName(username);
        if (u == null) {
            //没被占用
            //注册
            if (!password.equals(rePwd)) {
                return Result.error("两次密码输入不一致");
            }
            ValueOperations<String, String> operations = stringRedisTemplate2.opsForValue();
            String redisToken = operations.get(email);
            try {
                if (redisToken == null) {
                    throw new RuntimeException();
                }
            } catch (Exception exception) {
                logger.error("Failed to retrieve verification code from Redis for email: {}", email, exception);
                return Result.error("验证码过期");
            }
            if (!redisToken.equals(code)) {
                return Result.error("验证码错误");
            }
            userService.register(username, nickname, email, password);
            return Result.success();
        } else {
            logger.warn("Username {} is already taken", username);
            //占用
            return Result.error("用户名·已被占用！");
        }


    }

    // Done
    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password) {
        logger.info("Received login request for username: {}", username);
        User loginUser = userService.findByUserName(username);
        //判断用户名是否存在
        if (loginUser == null) {
            logger.warn("User with username {} not found", username);
            return Result.error("用户名错误！");
        } else {
            if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
                //登录成功
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", loginUser.getId());
                claims.put("username", loginUser.getUsername());
                String tocken = JwtUtil.genToken(claims);
//                Redis相关内容，为了方便测试，最后一次版本取消注释即可
                ValueOperations<String, String> operations = stringRedisTemplate1.opsForValue();
                operations.set(tocken, tocken, 24, TimeUnit.HOURS);
                logger.info("User {} logged in successfully", username);
                return Result.success(tocken);
            } else {
                logger.warn("Incorrect password for user {}", username);
                return Result.error("密码错误！");
            }
        }
    }

    // Done
    @GetMapping("/info")
    public Result<User> userInfo() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    // Done
    @PutMapping("/update")
    public Result update(@Pattern(regexp = "^\\S{5,16}$") String username, String nickname, String email, @RequestHeader("Authorization") String token) {
        // 获得当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        String nowUsername = (String) map.get("username");
        User nowUser = userService.findByUserName(nowUsername);

        // 修改当前用户username
        map.put("username", username);

        // 修改token
        String tocken = JwtUtil.genToken(map);
        ValueOperations<String, String> operations = stringRedisTemplate1.opsForValue();
        // 删除原有token
        operations.getOperations().delete(token);
        // 设置新token
        operations.set(tocken, tocken, 24, TimeUnit.HOURS);

        userService.update(nowUser.getId(), username, nickname, email);
        return Result.success(tocken);
    }

    // Done
    @PatchMapping("/update-avatar")
    public Result updateAvatar(@RequestParam MultipartFile file) {
        try {
            userService.updateAvatar(file);
            return Result.success();
        } catch (IOException e) {
            logger.error("Failed to update avatar", e);
            return Result.error(e.getMessage());
        }
    }

    @PatchMapping("/update-password")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        System.out.println(username);
        User loginUser = userService.findByUserName(username);
        if (!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码填写错误");
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次密码填写不一致");
        }
        String id = loginUser.getId();
        userService.updatePwd(id, newPwd);
        //Redis相关内容，为了方便测试，最后一次版本取消注释即可
        ValueOperations<String, String> operations = stringRedisTemplate1.opsForValue();
        // 删除现有token，用户需要重新登陆
        operations.getOperations().delete(token);
        return Result.success();
    }


    @PostMapping("/send-verification-code")//注册时候使用的发送验证码（和通过邮箱找回密码的不是一个）
    public Result sendVerificationCode(String email) {
        String code = generateRandomCodeUtil.getRandomCode();
        ValueOperations<String, String> operations = stringRedisTemplate2.opsForValue();
        operations.set(email, code, 1, TimeUnit.HOURS);
        System.out.println(operations);
        boolean flage = userService.sendVerificationCode(email, code);
        if (!flage) {
            logger.error("Failed to send verification code to email: {}", email);
            return Result.error("发送失败");
        }
        return Result.success();

    }

    @PostMapping("/send-verification-code/recovery")//用来通过邮箱找回密码发送验证码（和注册时候使用的发送验证码的发不是一个）
    public Result rpSendVerificationCode(String username, String email) {
        User user = userService.findByUserName(username);
        if (user == null || !user.getEmail().equals(email)) {
            return Result.error("信息不一致");
        }
        String code = generateRandomCodeUtil.getRandomCode();
        ValueOperations<String, String> operations = stringRedisTemplate2.opsForValue();
        operations.set(email, code, 1, TimeUnit.HOURS);
        System.out.println(operations);
        boolean flage = userService.sendVerificationCode(email, code);
        if (!flage) {
            logger.error("Failed to send verification code to email: {}", email);
            return Result.error("发送失败");
        }
        return Result.success();

    }

    @PostMapping("/retrieve-password")
    public Result retrievePassword(String username, String email, String newPwd, String rePwd, String code) {
        // 从数据库或缓存中获取对应邮箱地址的验证码
        // Redis相关内容，为了方便测试，最后一次版本取消注释即可
        ValueOperations<String, String> operations = stringRedisTemplate2.opsForValue();
        String redisToken = operations.get(email);
        try {
            if (redisToken == null) {
                throw new RuntimeException();
            }
        } catch (Exception exception) {
            logger.error("Failed to retrieve verification code from Redis for email: {}", email, exception);
            return Result.error("验证码过期");
        }
        if (!redisToken.equals(code)) {
            return Result.error("验证码错误");
        }
        if (!StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数");
        }
        if (!newPwd.equals(rePwd)) {
            return Result.error("两次密码填写不一致");
        }
        User user = userService.findByUserName(username);
        String id = user.getId();
        userService.updatePwd(id, newPwd);
        return Result.success();

    }


}