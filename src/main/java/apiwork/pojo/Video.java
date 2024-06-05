package apiwork.pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@TableName("video")
@Data
public class Video {
    @TableField("user_id")
    private String userId;
    @TableField("video_id")
    private String videoId;
    private String name;
    private String url;
    @TableField("digg_count")
    private Integer diggCount;
    @TableField("create_time")
    private LocalDateTime createTime;   //创建时间
}
