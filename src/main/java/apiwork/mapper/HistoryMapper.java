package apiwork.mapper;

import apiwork.pojo.History;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryMapper extends BaseMapper<History> {
    // 按照用户id获得浏览历史
    @Select("select * from history where user_id=#{userId}")
    List<History> selectHistoryByUserId(String userId);

    // 更新历史查看时间
    @Update("update history set create_time=#{time} where user_id=#{userId} and video_id=#{videoId}")
    void updateHistoryTime(String userId, String videoId, LocalDateTime time);

    @Select("select * from history where user_id=#{userId} and video_id=#{videoId}")
    History selectHistoryByUserIdAndVideoId(String userId, String videoId);
}
