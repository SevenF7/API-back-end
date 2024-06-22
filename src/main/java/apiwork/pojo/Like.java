package apiwork.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("Likes")
@Data
public class Like {
    @TableField("user_id")
    private String userId;
    @TableField("video_id")
    private String videoId;
    @TableField("create_time")
    private LocalDateTime createTime;   //查看时间
}
