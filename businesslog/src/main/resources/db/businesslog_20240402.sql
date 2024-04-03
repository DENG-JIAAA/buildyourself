-- DengJia 2024-04-02 10:31 业务数据日志表
create table business_log (
    id           bigint                             not null comment '操作日志主键id' primary key,
    table_name   varchar(50)                        null comment '表名',
    company_id   bigint                             null comment '单位id',
    company_name varchar(50)                        null comment '单位名称',
    log_title    varchar(150)                       null comment '日志标题',
    change_field varchar(50)                        null comment '改变字段名',
    log_type     tinyint                            null comment '日志类型（1UPDATE、2DELETE、3INSERT）',
    busi_type    tinyint                            null comment '操作类型（如：营业状态变更、行政区划变更... 具体请参考：RecordOperationTypeEnum）',
    request_url  varchar(50)                        null comment '请求的地址',
    before_info  text                               null comment '变更前信息',
    after_info   text                               null comment '变更后信息',
    ip           varchar(20)                        null comment '请求IP',
    handle_time  bigint                             null comment '请求处理时长（毫秒）',
    create_date  datetime default CURRENT_TIMESTAMP null comment '操作时间',
    create_by    bigint unsigned                    null comment '操作人ID',
    timestamp    bigint                             null comment '时间戳'
) comment '业务数据日志表' charset = utf8;
