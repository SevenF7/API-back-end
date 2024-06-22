package apiwork.mapper;

import apiwork.pojo.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Select("select * from video where video_id=#{videoId}")
    Video selectVideoByVideoId(String videoId);

    @Delete("delete from video where video_id=#{videoId}")
    void deleteVideoByVideoId(String videoId);

    @Select("select * from video order by digg_count desc")
    List<Video> selectAllVideoOrderByDigg();

    @Select("update video set digg_count = digg_count + 1 where video_id=#{videoId}")
    void updateIncreaseVideoLike(String videoId);

    @Select("update video set digg_count = digg_count - 1 where video_id=#{videoId}")
    void updateReduceVideoLike(String videoId);
}
