package apiwork.service.impl;

import apiwork.mapper.VideoMapper;
import apiwork.pojo.PageBean;
import apiwork.pojo.User;
import apiwork.pojo.Video;
import apiwork.service.VideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    //private static final String VIDEO_DIR = "./video/";

    @Value("${uploadFile.location}")
    private String uploadFileLocation;//上传文件保存的本地目录，使用@Value获取全局配置文件中配置的属性值

    @Override
    public Video findVideoByVideoId(String videoId) {
        return videoMapper.selectVideoByVideoId(videoId);
    }

    @Override
    public void uploadVideo(Video video, MultipartFile file) throws IOException {
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String videoId = UUID.randomUUID().toString();
        String newFilename = videoId + fileExtension;

        // 创建目标文件
        File targetFile = new File(uploadFileLocation + newFilename);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs(); // 创建目录
        }

        // 保存文件
        file.transferTo(targetFile);

        // 设置视频URL
        video.setVideoId(videoId);
        video.setUrl(uploadFileLocation + newFilename);
        video.setCreateTime(LocalDateTime.now());

        // 保存视频信息到数据库
        videoMapper.insert(video);
    }

    @Override
    public void deleteVideo(String videoId) {
        Video video = videoMapper.selectVideoByVideoId(videoId);
        if (video != null) {
            // 删除本地文件
            File file = new File(video.getUrl());
            if (file.exists()) {
                file.delete();
            }
            // 删除数据库记录
            videoMapper.deleteVideoByVideoId(videoId);
        }
    }

//    @Override
//    public List<Video> getAllVideosByUserId(String userId) {
//        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("user_id", userId);
//        return videoMapper.selectList(queryWrapper);
//    }

    @Override
    public PageBean<Video> getAllVideosByUserId(String userId, int page, int pageSize) {
        IPage<Video> videoPage = new Page<>(page, pageSize);
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);

        IPage<Video> resultPage = videoMapper.selectPage(videoPage, queryWrapper);
        return new PageBean<>(resultPage.getTotal(), resultPage.getRecords());
    }

    @Override
    public List<Video> getAllRecommendedVideos(String userId) {
        List<Video> video_list = videoMapper.selectAllVideoOrderByDigg();

        return video_list;
    }
}
