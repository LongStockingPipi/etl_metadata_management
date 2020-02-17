CREATE TABLE `t_etl_metadata_external_platform` (
  `p_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '平台ID（唯一）',
  `type_code` bigint(20) NOT NULL COMMENT '平台类型',
  `p_name` varchar(128) NOT NULL COMMENT '平台名称',
  `connect_url` varchar(128) DEFAULT NULL COMMENT '数据库连接url',
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `passwd` varchar(64) DEFAULT NULL COMMENT '密码',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `updated_by` bigint(20) NOT NULL COMMENT '更新人',
  `comments` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`p_id`),
  UNIQUE KEY `AK_NAME` (`p_name`),
  UNIQUE KEY `AK_Key_3` (`connect_url`,`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_etl_metadata_external_schema` (
  `s_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库ID（唯一）',
  `platform_id` bigint(20) NOT NULL COMMENT '所属平台ID',
  `s_name` varchar(128) NOT NULL COMMENT '库名',
  `s_c_name` varchar(128) NOT NULL COMMENT '中文名',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `updated_by` bigint(20) NOT NULL COMMENT '更新人',
  `comments` varchar(1024) DEFAULT NULL COMMENT '备注',
  `init_commands` varchar(1024) DEFAULT NULL COMMENT '初始化脚本',
  PRIMARY KEY (`s_id`),
  UNIQUE KEY `AK_NAME` (`s_name`),
  UNIQUE KEY `AK_PLTFRMID_CODE` (`platform_id`,`s_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_etl_metadata_external_table` (
  `t_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表ID（唯一）',
  `t_name` varchar(256) NOT NULL COMMENT '表名',
  `t_c_name` varchar(128) NOT NULL COMMENT '中文名',
  `schema_id` bigint(20) NOT NULL COMMENT '所属库id',
  `is_writable` tinyint(4) NOT NULL COMMENT '是都可写',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `updated_by` bigint(20) NOT NULL COMMENT '更新时间',
  `comments` varchar(1024) DEFAULT NULL COMMENT '备注',
  `type_code` int(11) NOT NULL COMMENT '表类型',
  PRIMARY KEY (`t_id`),
  UNIQUE KEY `AK_SCHEMAID_CODE` (`schema_id`,`t_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `t_etl_metadata_external_column` (
  `c_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字段ID（唯一）',
  `c_name` varchar(128) NOT NULL COMMENT '字段名',
  `c_c_name` varchar(128) DEFAULT NULL COMMENT '中文名',
  `type_code` bigint(20) NOT NULL COMMENT '字段类型',
  `is_partition_field` bigint(20) NOT NULL COMMENT '是否是分区字段',
  `is_primary_key` bigint(20) NOT NULL COMMENT '是否是主键',
  `is_nullable` bigint(20) NOT NULL COMMENT '是否可为空',
  `max_length` bigint(20) DEFAULT NULL COMMENT '最大长度',
  `numeric_scale` bigint(20) DEFAULT NULL COMMENT '小数点位数',
  `table_id` bigint(20) NOT NULL,
  `position` bigint(20) NOT NULL COMMENT '序号',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `created_by` bigint(20) NOT NULL COMMENT '创建人',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `updated_by` bigint(20) NOT NULL COMMENT '更新人',
  `comments` varchar(1024) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `AK_TABID_CODE` (`table_id`,`c_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

