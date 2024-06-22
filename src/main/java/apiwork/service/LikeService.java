package apiwork.service;

import apiwork.pojo.Like;

import java.time.LocalDateTime;
import java.util.List;

public interface LikeService {
    // 添加用户点赞
    void addLike(String userId, String videoId);

    // 按照视频id返回点赞用户
    List<Like> getAllLikeByVideoId(String videoId);

    // 更新点赞时间
    void updateLikeTime(String userId, String videoId, LocalDateTime time);

    // 查找是否存在该点赞
    boolean isHaveLike(String userId, String videoId);

    // 删除点赞信息
    void deleteLike(String userId, String videoId);
}
