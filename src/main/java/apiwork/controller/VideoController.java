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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/video")
public class VideoController {

    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

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
        logger.info("Received request to get video with ID: {}", videoId);
        Video video = videoService.findVideoByVideoId(videoId);
        if (video != null) {
            logger.info("Video found: {}", video);
        } else {
            logger.warn("Video with ID {} not found", videoId);
        }
        return video;
    }

    @PostMapping("/upload")
    public Result uploadVideo(@RequestParam("userId") String userId,
                              @RequestParam("name") String name,
                              @RequestParam("file") MultipartFile file) {
        if (Objects.equals(userId, "")) {
            Map<String, Object> map = ThreadLocalUtil.get();
            String username = (String) map.get("username");
            userId = userService.findByUserName(username).getId();
        }
        logger.info("Received request to upload video: userId={}, name={}", userId, name);
        try {
            Video video = new Video();
            video.setUserId(userId);
            video.setName(name);
            video.setDiggCount(0);
            videoService.uploadVideo(video, file);
            logger.info("Video uploaded successfully: {}", video);
            return Result.success();
        } catch (IOException e) {
            logger.error("Error uploading video: {}", e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{videoId}")
    public Result deleteVideo(@PathVariable String videoId) {
        logger.info("Received request to delete video with ID: {}", videoId);
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();
        String videoUserId = videoService.findVideoByVideoId(videoId).getUserId();

        if (currentUserId.equals(videoUserId)) {
            videoService.deleteVideo(videoId);
            logger.info("Video with ID {} deleted successfully by user {}", videoId, currentUserId);
            return Result.success("删除成功");
        } else {
            logger.warn("User {} attempted to delete video with ID {}, but does not have permission", currentUserId, videoId);
            return Result.error("权限等级不足，删除失败");
        }
    }

    @GetMapping("/user/{userId}")
    public PageBean<Video> getAllVideosByUserId(@PathVariable String userId,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        logger.info("Received request to get all videos for userId={}, page={}, pageSize={}", userId, page, pageSize);
        return videoService.getAllVideosByUserId(userId, page, pageSize);
    }

    @GetMapping("/recommend")
    public List<Video> getRecommendVideos() {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        logger.info("Received request to get recommended videos for userId={}", currentUserId);
        List<Video> videoList = videoService.getAllRecommendedVideos(currentUserId);
        videoList.removeIf(video -> historyService.isHaveHistory(currentUserId, video.getVideoId()));

        logger.info("Returning {} recommended videos for userId={}", videoList.size(), currentUserId);
        return videoList;
    }

    @PostMapping("/like")
    public Result likeVideo(@RequestParam String videoId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        logger.info("User {} likes video with ID {}", currentUserId, videoId);
        if (likeService.isHaveLike(currentUserId, videoId)) {
            logger.warn("User {} has already liked video with ID {}", currentUserId, videoId);
            return Result.error("您已经点过赞了！");
        } else {
            videoService.updateIncreaseVideoLike(videoId);
            likeService.addLike(currentUserId, videoId);
            logger.info("Video with ID {} liked successfully by user {}", videoId, currentUserId);
            return Result.success("点赞成功");
        }
    }

    @PostMapping("/dislike")
    public Result dislikeVideo(@RequestParam String videoId) {
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        logger.info("User {} dislikes video with ID {}", currentUserId, videoId);
        if (likeService.isHaveLike(currentUserId, videoId)) {
            videoService.updateReduceVideoLike(videoId);
            likeService.deleteLike(currentUserId, videoId);
            logger.info("Video with ID {} disliked successfully by user {}", videoId, currentUserId);
            return Result.success();
        } else {
            logger.warn("User {} attempted to dislike video with ID {}, but has not liked it", currentUserId, videoId);
            return Result.error("取消点赞失败！");
        }
    }
}
//package apiwork.controller;
//
//import apiwork.pojo.PageBean;
//import apiwork.pojo.Result;
//import apiwork.pojo.User;
//import apiwork.pojo.Video;
//import apiwork.service.HistoryService;
//import apiwork.service.LikeService;
//import apiwork.service.UserService;
//import apiwork.service.VideoService;
//import apiwork.utils.ThreadLocalUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/video")
//public class VideoController {
//
//    @Autowired
//    private VideoService videoService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private HistoryService historyService;
//
//    @Autowired
//    private LikeService likeService;
//
//    @GetMapping("/")
//    public Video getVideoByVideoId(@RequestParam String videoId) {
//        return videoService.findVideoByVideoId(videoId);
//    }
//
//    @PostMapping("/upload")
//    public Result uploadVideo(@RequestParam("userId") String userId,
//                              @RequestParam("name") String name,
//                              @RequestParam("file") MultipartFile file) {
//        try {
//
////            Map<String, Object> map = ThreadLocalUtil.get();
////            String username = (String) map.get("username");
////            User user = userService.findByUserName(username);
//
//            Video video = new Video();
//            video.setUserId(userId);
//            video.setName(name);
//            video.setDiggCount(0);
//            videoService.uploadVideo(video, file);
//            return Result.success();
//        } catch (IOException e) {
//            return Result.error(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/delete/{videoId}")
//    public Result deleteVideo(@PathVariable String videoId) {
//        // 查找当前登录的用户
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String username = (String) map.get("username");
//        String currentUserId = userService.findByUserName(username).getId();
//
//        // 查找删除视频的用户
//        String videoUserId = videoService.findVideoByVideoId(videoId).getUserId();
//
//        // 如果相同，删除视频
//        if (currentUserId.equals(videoUserId)) {
//            videoService.deleteVideo(videoId);
//            return Result.success("删除成功");
//        }
//        else {
//            return Result.error("权限等级不足，删除失败");
//        }
//
//
//    }
//
////    @GetMapping("/user/{userId}")
////    public List<Video> getAllVideosByUserId(@PathVariable String userId) {
////        return videoService.getAllVideosByUserId(userId);
////    }
//    @GetMapping("/user/{userId}")
//    public PageBean<Video> getAllVideosByUserId(@PathVariable String userId,
//                                                @RequestParam(defaultValue = "1") int page,
//                                                @RequestParam(defaultValue = "10") int pageSize) {
//        return videoService.getAllVideosByUserId(userId, page, pageSize);
//    }
//
//    @GetMapping("/recommend")
//    public List<Video> getRecommendVideos() {
//        // 查找当前登录的用户
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String username = (String) map.get("username");
//        String currentUserId = userService.findByUserName(username).getId();
//
//        List<Video> video_list = videoService.getAllRecommendedVideos(currentUserId);
//        video_list.removeIf(video -> historyService.isHaveHistory(currentUserId, video.getVideoId()));
//
//        return video_list;
//    }
//
//    // 实现点赞
//    @PostMapping("/like")
//    public Result likeVideo(@RequestParam String videoId) {
//        // 查找当前登录的用户
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String username = (String) map.get("username");
//        String currentUserId = userService.findByUserName(username).getId();
//
//        // 该用户是否已经点过赞了
//        if (likeService.isHaveLike(currentUserId, videoId)) {
//            // 如果已经点过赞，返回错误
//            return Result.error("您已经点过赞了！");
//        }
//        else {
//            // 未点赞，则点赞数增加并添加到点赞表
//            videoService.updateIncreaseVideoLike(videoId);
//            likeService.addLike(currentUserId, videoId);
//            return Result.success("点赞成功");
//        }
//    }
//
//    @PostMapping("/dislike")
//    public Result dislikeVideo(@RequestParam String videoId) {
//        // 查找当前登录的用户
//        Map<String, Object> map = ThreadLocalUtil.get();
//        String username = (String) map.get("username");
//        String currentUserId = userService.findByUserName(username).getId();
//
//        // 该用户是否已经点过赞了
//        if (likeService.isHaveLike(currentUserId, videoId)) {
//            // 如果已经点过赞，删除点赞信息，点赞数减一
//            videoService.updateReduceVideoLike(videoId);
//            likeService.deleteLike(currentUserId, videoId);
//            return Result.success();
//        }
//        else {
//            // 未点赞，则返回错误
//            return Result.error("取消点赞失败！");
//        }
//    }
//}
//
