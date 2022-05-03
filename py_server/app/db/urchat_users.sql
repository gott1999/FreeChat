CREATE TABLE `user_b_data`  (
  `uid` int UNSIGNED NOT NULL COMMENT 'unique id',
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户自定义 id',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '显示名',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '用户头像',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `index_user_b_data_uid`(`uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `user_relationship`  (
  `src_uid` int UNSIGNED NOT NULL COMMENT '源uid',
  `tar_uid` int UNSIGNED NOT NULL COMMENT '目标uid',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '源id对目标id的绰号',
  INDEX `index_user_relationship_src_uid`(`src_uid` ASC) USING BTREE,
  INDEX `index_user_relationship_tar_uid`(`tar_uid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

CREATE TABLE `user_u_data`  (
  `uid` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'unique Id',
  `uname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'unique 名称',
  `upassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'unique 密码',
  `del_time` datetime NULL DEFAULT NULL COMMENT '删除时间 空为未删除',
  PRIMARY KEY (`uid`) USING BTREE,
  UNIQUE INDEX `index_user_u_data_uid`(`uid` ASC) USING BTREE COMMENT '关于uid的索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

ALTER TABLE `user_b_data` ADD CONSTRAINT `fk_user_b_uid` FOREIGN KEY (`uid`) REFERENCES `user_u_data` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `user_relationship` ADD CONSTRAINT `fk_user_relationship_src_uid` FOREIGN KEY (`src_uid`) REFERENCES `user_u_data` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT;
ALTER TABLE `user_relationship` ADD CONSTRAINT `fk_user_relationship_tar_uid` FOREIGN KEY (`tar_uid`) REFERENCES `user_u_data` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT;

CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `user_base_data` AS select `user_u_data`.`uname` AS `uname`,`user_u_data`.`upassword` AS `upassword`,`user_u_data`.`del_time` AS `del_time`,`user_b_data`.`display_name` AS `display_name`,`user_u_data`.`uid` AS `uid`,`user_b_data`.`id` AS `id` from (`user_u_data` join `user_b_data` on((`user_u_data`.`uid` = `user_b_data`.`uid`)));

