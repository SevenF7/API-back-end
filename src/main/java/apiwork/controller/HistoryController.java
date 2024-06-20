package apiwork.controller;


import apiwork.pojo.History;
import apiwork.pojo.Result;
import apiwork.pojo.User;
import apiwork.service.HistoryService;
import apiwork.service.UserService;
import apiwork.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public Result addHistory(@RequestParam String videoId) {
        // 查找当前登录的用户
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        String currentUserId = userService.findByUserName(username).getId();

        // 查找用户是否看过该视频
        if (historyService.isHaveHistory(currentUserId, videoId)) {
            // 若存在，更新查看时间
            LocalDateTime time = LocalDateTime.now();
            historyService.updateHistoryTime(currentUserId, videoId, time);
        }
        else {
            // 不存在则添加
            historyService.addHistory(currentUserId, videoId);
        }

        return Result.success();
    }

    @GetMapping("/get")
    public List<History> getHistory(@RequestParam String userId) {
        return historyService.getAllHistoryByUserId(userId);
    }
}
