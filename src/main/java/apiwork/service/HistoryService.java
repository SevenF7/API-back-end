package apiwork.service;

import apiwork.pojo.History;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoryService {
    // 添加用户观看历史
    void addHistory(String userId, String videoId);

    // 按照用户id返回观看历史
    List<History> getAllHistoryByUserId(String userId);

    // 更新历史查看时间
    void updateHistoryTime(String userId, String videoId, LocalDateTime time);

    // 查找是否存在该历史
    boolean isHaveHistory(String userId, String videoId);
}
