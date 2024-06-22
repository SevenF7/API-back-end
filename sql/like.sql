DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `user_id` varchar(255) NOT NULL,
  `video_id` varchar(255) NOT NULL,
  `create_time` datetime NULL DEFAULT (CURDATE()),
  PRIMARY KEY (`video_id`, `user_id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
	FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
	FOREIGN KEY (`video_id`) REFERENCES `video` (`video_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) 