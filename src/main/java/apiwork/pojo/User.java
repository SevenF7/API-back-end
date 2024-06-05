package apiwork.pojo;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
//lombook 在编译阶段，为实体类自动生成setter getter toString方法
//在pomm文件中引入依赖  在实体类上添加注解
@Data
@TableName("user")
public class User {
    @NotNull
    private String id;//主键ID
    private String username;//用户名
    private String nickname;//昵称
    @JsonIgnore
    private String password;//密码
    @Email
    private String email;//邮箱
    @TableField("createTime")
    private LocalDateTime createTime;//创建时间
    @TableField("userPic")
    private String userPic;//用户头像地址
}
