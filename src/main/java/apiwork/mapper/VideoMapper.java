package apiwork.mapper;

import apiwork.pojo.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Select("select * from video where video_id=#{videoId}")
    Video selectVideoByVideoId(String videoId);

    @Delete("delete from video where video_id=#{videoId}")
    void deleteVideoByVideoId(String videoId);
}
