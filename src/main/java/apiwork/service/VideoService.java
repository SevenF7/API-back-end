package apiwork.service;

import apiwork.pojo.PageBean;
import apiwork.pojo.Video;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface VideoService {
    // 按照video_id查找
    Video findVideoByVideoId(String videoId);

    // 上传视频
    void uploadVideo(Video video, MultipartFile file) throws IOException;

    // 删除视频
    void deleteVideo(String videoId);

    // 查看我的所有视频
//    List<Video> getAllVideosByUserId(String userId);

    // 分页查看我的所有视频
    PageBean<Video> getAllVideosByUserId(String userId, int page, int pageSize);
}
