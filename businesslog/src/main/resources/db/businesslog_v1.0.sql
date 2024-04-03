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

INSERT INTO db.company (id, name, company_type, is_test, short_name, father_dept, setting_time, unified_social_cc, seq, social_cc_state, company_contact_tel, company_contact_email, company_contact_fax, area_id, supervision_unit, supervision_area_id, register_address, register_post, company_postal_adress, work_address, register_type, industry_category, occupational_risk_classify, staff_count, pickup_count, main_opera_income, register_capital, scale, year_assets, total_assets, legal_name, legal_tel, chemical_emergency_phone, occ_leader_sid, occ_leader_tel, occ_manager_name, occ_manager_tel, main_leader_name, main_leader_tel, create_date, create_by, update_date, update_by, is_valid, data_source, data_source_id, logo_file_url, corp_cc_extend, lng, lat, punish_num, is_dec, is_dec_current_year, is_report, belong_to_region, is_reliable, is_exist_radioactivity, is_supervise, spec_sort, last_year_pick_up_count) VALUES (99657571579429867, '宜康（杭州）生物技术有限公司', 2, false, '宜康有限公司1', null, null, '913301007494970550', 000, 0, null, null, null, 330102001000, 3, 330102000000, '浙江省杭州市莫干山路1418-32号2幢三层、四层（上城科技工业基地）', null, null, '莫干山路1418-32号2幢三层、四层（上城科技工业基地）', 11, 3944, 2, 350, 22, null, null, '2', null, null, '于君丽', '15821954789', null, null, null, '葛挺', '13958010417', '于君丽', '15821954789', '2022-04-18 17:16:05', 1, '2022-07-13 15:37:35', 3418757818713088, true, 2, 'caff8fd710dd466da07c83b36dc8bf61', null, null, '120.09593', '30.34216', 0, true, false, true, null, true, false, true, 1, null);
