package apiwork.controller;

import apiwork.pojo.PageBean;
import apiwork.pojo.Result;
import apiwork.pojo.User;
import apiwork.pojo.Video;
import apiwork.service.HistoryService;
import apiwork.service.LikeService;
import apiwork.service.UserService;
import apiwork.service.VideoService;
import apiwork.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/")
    public Video getVideoByVideoId(@RequestParam String videoId) {
        return videoService.findVideoByVideoId(videoId);
    }

    @PostMapping("/upload")
    public Result uploadVideo(@RequestParam("userId") String userId,
                              @RequestParam("name") String name,
                              @RequestParam("file") MultipartFile file) {
        try {

//            Map<String, Object> map = ThreadLocalUtil.get();
//            String username = (String) map.get("username");
//            User user = userService.findByUserName(username);

            Video video = new Video();
            video.setUserId(userId);
            video.setName(name);
            video.setDiggCount(0);
            videoService.uploadVideo(video, file);
            return Result.success();
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{videoId}")
    public Result deleteVideo(@PathVariable String videoId) {
        // 查找当前登录的用户
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        // 查找删除视频的用户
        String videoUserId = videoService.findVideoByVideoId(videoId).getUserId();

        // 如果相同，删除视频
        if (currentUserId.equals(videoUserId)) {
            videoService.deleteVideo(videoId);
            return Result.success("删除成功");
        }
        else {
            return Result.error("权限等级不足，删除失败");
        }


    }

//    @GetMapping("/user/{userId}")
//    public List<Video> getAllVideosByUserId(@PathVariable String userId) {
//        return videoService.getAllVideosByUserId(userId);
//    }
    @GetMapping("/user/{userId}")
    public PageBean<Video> getAllVideosByUserId(@PathVariable String userId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return videoService.getAllVideosByUserId(userId, page, pageSize);
    }

    @GetMapping("/recommend")
    public List<Video> getRecommendVideos() {
        // 查找当前登录的用户
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        List<Video> video_list = videoService.getAllRecommendedVideos(currentUserId);
        video_list.removeIf(video -> historyService.isHaveHistory(currentUserId, video.getVideoId()));

        return video_list;
    }

    // 实现点赞
    @PostMapping("/like")
    public Result likeVideo(@RequestParam String videoId) {
        // 查找当前登录的用户
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        // 该用户是否已经点过赞了
        if (likeService.isHaveLike(currentUserId, videoId)) {
            // 如果已经点过赞，返回错误
            return Result.error("您已经点过赞了！");
        }
        else {
            // 未点赞，则点赞数增加并添加到点赞表
            videoService.updateIncreaseVideoLike(videoId);
            likeService.addLike(currentUserId, videoId);
            return Result.success("点赞成功");
        }
    }

    @PostMapping("/dislike")
    public Result dislikeVideo(@RequestParam String videoId) {
        // 查找当前登录的用户
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        // 该用户是否已经点过赞了
        if (likeService.isHaveLike(currentUserId, videoId)) {
            // 如果已经点过赞，删除点赞信息，点赞数减一
            videoService.updateReduceVideoLike(videoId);
            likeService.deleteLike(currentUserId, videoId);
            return Result.success();
        }
        else {
            // 未点赞，则返回错误
            return Result.error("取消点赞失败！");
        }
    }
}

