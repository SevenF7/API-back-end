package apiwork.service.impl;

import apiwork.mapper.LikeMapper;
import apiwork.pojo.Like;
import apiwork.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public void addLike(String userId, String videoId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setVideoId(videoId);
        like.setCreateTime(LocalDateTime.now());

        // 插入数据
        likeMapper.insert(like);
    }

    @Override
    public void deleteLike(String userId, String videoId) {
        likeMapper.deleteLikeByUserIdAndVideoId(userId, videoId);
    }

    @Override
    public List<Like> getAllLikeByVideoId(String videoId) {
        return likeMapper.selectLikeByVideoId(videoId);
    }

    @Override
    public void updateLikeTime(String userId, String videoId, LocalDateTime time) {
        likeMapper.updateLikeTime(userId, videoId, time);
    }

    @Override
    public boolean isHaveLike(String userId, String videoId) {
        return likeMapper.selectLikeByUserIdAndVideoId(userId, videoId) != null;
    }
}
