package apiwork.mapper;

import apiwork.pojo.Like;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

public interface LikeMapper extends BaseMapper<Like> {
    // 按照视频id获得点赞列表
    @Select("select * from `like` where video_id=#{videoId}")
    List<Like> selectLikeByVideoId(String videoId);

    // 更新点赞时间
    @Update("update likes set create_time=#{time} where user_id=#{userId} and video_id=#{videoId}")
    void updateLikeTime(String userId, String videoId, LocalDateTime time);

    @Select("select * from likes where user_id=#{userId} and video_id=#{videoId}")
    Like selectLikeByUserIdAndVideoId(String userId, String videoId);

    @Delete("delete from likes where user_id=#{userId} and video_id=#{videoId}")
    void deleteLikeByUserIdAndVideoId(String userId, String videoId);
}
