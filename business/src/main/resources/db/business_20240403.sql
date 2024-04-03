
-- DengJia 2024-04-3 15:24 业务表
create table company (
    id                         bigint unsigned                        not null comment 'ID' primary key,
    name                       varchar(100)                           null comment 'fullname企业名称',
    company_type               tinyint                   default 1    not null comment '企业类型（1-用人单位,2-外包单位,3-用人兼外包单位）',
    is_test                    bit                       default b'0' null comment '是否是测试企业',
    short_name                 varchar(100)                           null comment '企业名称简称',
    father_dept                varchar(100)                           null comment '上属公司（主管单位）',
    setting_time               date                                   null comment '成立时间',
    unified_social_cc          varchar(100)              default ''   null comment '统一社会信用代码',
    seq                        tinyint unsigned zerofill default 0    null comment '顺序码',
    social_cc_state            tinyint(1)                default 0    null comment '信用代码状态（0默认， 2信用代码错误）',
    company_contact_tel        varchar(50)                            null comment 'corp_tel 企业联系电话	',
    company_contact_email      varchar(50)                            null comment 'corp_email 企业电子邮箱',
    company_contact_fax        varchar(20)                            null comment '企业传真',
    area_id                    bigint unsigned                        null comment 'corp_area_id 行政区域区编号',
    supervision_unit           bigint                                 null comment '监督单位',
    supervision_area_id        bigint                                 null comment '监督单位区域',
    register_address           varchar(100)                           null comment '企业注册地详址',
    register_post              varchar(6)                             null comment '企业注册地址邮编',
    company_postal_adress      varchar(500)                           null comment 'corp_address 企业通讯地址',
    work_address               varchar(100)                           null comment '工作产所地址（工作场所地址）',
    register_type              bigint                                 null comment '注册类型',
    industry_category          bigint                                 null comment 'corp_type 行业分类（sys_param.param_code）',
    occupational_risk_classify tinyint                                null comment '职业病危害风险分类（0-严重、1-较重、2-一般）',
    staff_count                int                       default 0    null comment '在岗职工人数',
    pickup_count               int                       default 0    null comment '接害人数（不重复计）',
    main_opera_income          decimal(12, 2)                         null comment '主营营业收入（万元）',
    register_capital           decimal(12, 2)                         null comment '注册资本',
    scale                      varchar(100)                           null comment '企业规模',
    year_assets                decimal(12, 2)                         null comment '企业年度产值',
    total_assets               decimal(12, 2)                         null comment '企业资产总值',
    legal_name                 varchar(100)                           null comment '法人代表姓名',
    legal_tel                  varchar(100)                           null comment '法人代表联系电话',
    chemical_emergency_phone   varchar(45)                            null comment '化学事故应急咨询服务电话',
    occ_leader_sid             bigint unsigned                        null comment '职业卫生负责人',
    occ_leader_tel             varchar(30)               default ''   null comment '职业卫生负责人联系电话',
    occ_manager_name           varchar(30)               default ''   null comment '职业卫生管理人员',
    occ_manager_tel            varchar(30)               default ''   null comment 'ְ管理人员手机号码',
    main_leader_name           varchar(128)              default ''   null comment '企业主要负责人',
    main_leader_tel            varchar(30)               default ''   null comment '负责人手机号码',
    create_date                datetime                               null comment '创建时间',
    create_by                  bigint unsigned                        null comment '创建人',
    update_date                datetime                               null comment '最后修改时间',
    update_by                  bigint unsigned                        null comment '最后修改人ID',
    is_valid                   bit                       default b'1' null comment '是否有效（0-无效，1-有效）',
    data_source                tinyint                                null comment '数据来源（CompanyDataSourceEnum约定）',
    data_source_id             varchar(128)                           null comment '数据来源ID（原ID）',
    logo_file_url              varchar(500)                           null comment '企业logo url 地址',
    corp_cc_extend             varchar(100)                           null comment '[平台字段]企业编号串扩展(社会统一信代码/登记号/组织代码)',
    lng                        varchar(20)               default ''   null comment '经度',
    lat                        varchar(20)               default ''   null comment '纬度',
    punish_num                 int                       default 0    null comment '处罚次数',
    is_dec                     bit                       default b'0' null comment '是否有被纳入分类监督企业过(0-否,1-是)',
    is_dec_current_year        bit                       default b'0' null comment '是否是今年分类监督企业(0-否,1-是)',
    is_report                  bit                       default b'0' null comment '是否申报(0-未申报,1-已申报)',
    belong_to_region           bigint                                 null comment '所属地区',
    is_reliable                bit                       default b'0' null comment '自查结果是否可靠(0-否,1-是)',
    is_exist_radioactivity     bit                       default b'0' null comment '是否存在放射性危害因素(0否，1是)',
    is_supervise               bit                       default b'0' null comment '是否是监督对象',
    spec_sort                  int                       default 1    null comment '特殊排序需求',
    is_show_supervise          bit as (if(((`is_valid` = 1) and (`company_type` = 1) and (`is_test` = 0)), 1, 0)) comment '优化查询使用-视图company_supervise_view中的结果',
    last_year_pick_up_count    varchar(255)                           null comment '记录去年四个来源的接害人数'
) comment '企业信息' charset = utf8;

create index `idx_ pickup_count` on company (pickup_count);
create index idx_decorg on company (company_type, area_id, is_test, is_valid, register_type);
create index idx_scale on company (company_type, area_id, scale);
create index idx_showsupervise on company (is_show_supervise, area_id, pickup_count);
create index idx_specbussstate on company (company_type, area_id, spec_sort, data_source, is_test);
create index index2 on company (name);
create index index6 on company (unified_social_cc);
create index inx_areaid_name_code_indu_valid on company (area_id, name, unified_social_cc, industry_category, is_valid);

-- DengJia 2024-04-3 15:25 业务表测试数据
INSERT INTO db.company (id, name, company_type, is_test, short_name, father_dept, setting_time, unified_social_cc, seq, social_cc_state, company_contact_tel, company_contact_email, company_contact_fax, area_id, supervision_unit, supervision_area_id, register_address, register_post, company_postal_adress, work_address, register_type, industry_category, occupational_risk_classify, staff_count, pickup_count, main_opera_income, register_capital, scale, year_assets, total_assets, legal_name, legal_tel, chemical_emergency_phone, occ_leader_sid, occ_leader_tel, occ_manager_name, occ_manager_tel, main_leader_name, main_leader_tel, create_date, create_by, update_date, update_by, is_valid, data_source, data_source_id, logo_file_url, corp_cc_extend, lng, lat, punish_num, is_dec, is_dec_current_year, is_report, belong_to_region, is_reliable, is_exist_radioactivity, is_supervise, spec_sort, last_year_pick_up_count) VALUES (99657571579429867, '宜康（杭州）生物技术有限公司', 2, false, '宜康有限公司1', null, null, '913301007494970550', 000, 0, null, null, null, 330102001000, 3, 330102000000, '浙江省杭州市莫干山路1418-32号2幢三层、四层（上城科技工业基地）', null, null, '莫干山路1418-32号2幢三层、四层（上城科技工业基地）', 11, 3944, 2, 350, 22, null, null, '2', null, null, '于君丽', '15821954789', null, null, null, '葛挺', '13958010417', '于君丽', '15821954789', '2022-04-18 17:16:05', 1, '2022-07-13 15:37:35', 3418757818713088, true, 2, 'caff8fd710dd466da07c83b36dc8bf61', null, null, '120.09593', '30.34216', 0, true, false, true, null, true, false, true, 1, null);
