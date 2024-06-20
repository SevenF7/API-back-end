package apiwork.service.impl;

import apiwork.mapper.HistoryMapper;
import apiwork.pojo.History;
import apiwork.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HistoryMapper historyMapper;

    // 添加观看历史
    @Override
    public void addHistory(String userId, String videoId) {
        History history = new History();
        history.setUserId(userId);
        history.setVideoId(videoId);
        history.setCreateTime(LocalDateTime.now());
        // 插入数据
        historyMapper.insert(history);
    }

    // 获得用户所有的历史记录
    @Override
    public List<History> getAllHistoryByUserId(String userId) {
        return historyMapper.selectHistoryByUserId(userId);
    }

    @Override
    public void updateHistoryTime(String userId, String videoId, LocalDateTime time) {
        historyMapper.updateHistoryTime(userId, videoId, time);
    }

    @Override
    public boolean isHaveHistory(String userId, String videoId) {
        return historyMapper.selectHistoryByUserIdAndVideoId(userId, videoId) != null;
    }
}
