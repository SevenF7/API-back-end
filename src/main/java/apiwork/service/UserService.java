package apiwork.service;

import apiwork.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    //根据用户名查询用户
    User findByUserName(String username);

    //注册
    void register(String username,String nickname,String email, String password);

    void update(String id, String username, String nickname, String email);

    void updateAvatar(MultipartFile file) throws IOException;

    void updatePwd(String id ,String newPwd);

    boolean sendVerificationCode(String email,String code);
}
