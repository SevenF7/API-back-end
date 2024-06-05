package apiwork.service.impl;

import apiwork.mapper.UserMapper;
import apiwork.pojo.User;
import apiwork.service.UserService;
import apiwork.utils.MailUtil;
import apiwork.utils.Md5Util;
import apiwork.utils.ThreadLocalUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailUtil mailUtil;
    @Value("${uploadAvatar.location}")
    private String uploadAvatarLocation;//上传文件保存的本地目录，使用@Value获取全局配置文件中配置的属性值

    @Override
    public User findByUserName(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void register(String username, String nickname, String email, String password) {
        // 加密
        String md5String = Md5Util.getMD5String(password);

        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEmail(email);
        user.setPassword(md5String);
        user.setCreateTime(LocalDateTime.now());
        System.out.println(user);
        // 添加
        userMapper.insert(user);
    }

    @Override
    public void update(String id, String username, String nickname, String email) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setNickname(nickname);
        user.setEmail(email);

        userMapper.updateById(user);
    }

    @Override
    public void updateAvatar(MultipartFile file) throws IOException {
        Map<String, Object> map = ThreadLocalUtil.get();
        String id = (String) map.get("id");
        User user = userMapper.selectById(id);

        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String picId = UUID.randomUUID().toString();
        String newFilename = picId + fileExtension;

        // 创建目标文件
        File targetFile = new File(uploadAvatarLocation + newFilename);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs(); // 创建目录
        }

        // 保存文件
        file.transferTo(targetFile);

        // 设置头像url
        user.setUserPic(uploadAvatarLocation + newFilename);

        // 保存到数据库
        userMapper.updateById(user);
    }

    @Override
    public void updatePwd(String id, String newPwd) {
        User user = userMapper.selectById(id);
        if (user != null) {
            user.setPassword(Md5Util.getMD5String(newPwd));
            userMapper.updateById(user);
        }
    }

    @Override
    public boolean sendVerificationCode(String email, String verificationCode) {
        try {
            mailUtil.sendVerificationCode(email, verificationCode);
            // 将验证码存入数据库或者缓存中，并设置过期时间，可以使用Spring Cache或Redis等
            // 如果需要验证验证码是否正确，从数据库或缓存中获取对应的验证码进行比较
        } catch (MessagingException ex) {
            return false;
        }
        return true;
    }
}
