-- 测试数据脚本
USE specimen_db;

-- 插入测试标本数据
INSERT INTO `specimen` (`specimen_no`, `name`, `latin_name`, `family`, `genus`, `collector_id`, `collection_date`, `location`, `longitude`, `latitude`, `description`, `status`)
VALUES
('SP001', '银杏', 'Ginkgo biloba', '银杏科', '银杏属', 1, '2025-10-15', '校园东门', 116.407526, 39.904989, '高大落叶乔木，叶片扇形', '已入库'),
('SP002', '梧桐', 'Firmiana simplex', '梧桐科', '梧桐属', 2, '2025-10-20', '校园西门', 116.397526, 39.914989, '落叶大乔木，树皮平滑', '已入库'),
('SP003', '樱花', 'Cerasus serrulata', '蔷薇科', '樱属', 1, '2025-09-25', '图书馆旁', 116.417526, 39.894989, '落叶乔木，春季开花', '已入库'),
('SP004', '松树', 'Pinus', '松科', '松属', 2, '2025-09-10', '操场北侧', 116.427526, 39.924989, '常绿乔木，针叶', '已入库'),
('SP005', '竹子', 'Bambusoideae', '禾本科', '竹亚科', 1, '2025-08-18', '后山竹林', 116.437526, 39.884989, '禾本科多年生植物', '已入库');

-- 为每个标本创建地图标注
INSERT INTO `map_marker` (`specimen_id`, `longitude`, `latitude`, `label`, `marker_color`)
SELECT `id`, `longitude`, `latitude`, `name`, '#4CAF50'
FROM `specimen`
WHERE `longitude` IS NOT NULL AND `latitude` IS NOT NULL;

-- 插入采集记录
INSERT INTO `collection_record` (`user_id`, `specimen_name`, `collection_time`, `longitude`, `latitude`, `remarks`)
VALUES
(2, '银杏', '2025-10-15 09:30:00', 116.407526, 39.904989, '发现于东门路边，生长良好'),
(2, '梧桐', '2025-10-20 14:15:00', 116.397526, 39.914989, '西门附近，树龄约20年'),
(2, '樱花', '2025-09-25 16:45:00', 116.417526, 39.894989, '图书馆旁边，花期将至'),
(2, '紫薇', '2025-09-05 10:20:00', 116.447526, 39.934989, '宿舍楼前，夏季开花'),
(2, '桂花', '2025-08-30 11:10:00', 116.387526, 39.874989, '食堂后面，香气浓郁');

-- 插入更多公告
INSERT INTO `announcement` (`title`, `content`)
VALUES
('秋季植物采集活动通知', '定于10月20日-10月30日开展秋季植物标本采集活动，欢迎师生参与。'),
('标本库开放时间调整', '从11月1日起，标本库开放时间调整为周一至周五 9:00-17:00。'),
('新增数字地图功能', '系统已上线数字地图功能，可查看校园植物分布情况。');
