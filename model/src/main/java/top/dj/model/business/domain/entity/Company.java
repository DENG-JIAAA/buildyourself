package top.dj.model.business.domain.entity;


import lombok.Data;

import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;

import java.util.Date;
import java.math.BigDecimal;


@Data
@Table(name = "company")
public class Company implements Serializable {

    @Serial
    private static final long serialVersionUID = -5473286164934800322L;

    /**
     * ID
     */
    private Long id;

    /**
     * fullname企业名称
     */
    private String name;

    /**
     * 企业类型（1-用人单位,2-外包单位,3-用人兼外包单位）
     */
    private Integer companyType;

    /**
     * 是否是测试企业
     */
    private Boolean isTest;

    /**
     * 企业名称简称
     */
    private String shortName;

    /**
     * 上属公司（主管单位）
     */
    private String fatherDept;

    /**
     * 成立时间
     */
    private Date settingTime;

    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCc;

    /**
     * 顺序码
     */
    private Integer seq;

    /**
     * 信用代码状态（0默认， 2信用代码错误）
     */
    private Integer socialCcState;

    /**
     * corp_tel 企业联系电话
     */
    private String companyContactTel;

    /**
     * corp_email 企业电子邮箱
     */
    private String companyContactEmail;

    /**
     * 企业传真
     */
    private String companyContactFax;

    /**
     * corp_area_id 行政区域区编号
     */
    private Long areaId;

    /**
     * 监督单位
     */
    private Long supervisionUnit;

    /**
     * 监督单位区域
     */
    private Long supervisionAreaId;

    /**
     * 企业注册地详址
     */
    private String registerAddress;

    /**
     * 企业注册地址邮编
     */
    private String registerPost;

    /**
     * corp_address 企业通讯地址
     */
    private String companyPostalAdress;

    /**
     * 工作产所地址（工作场所地址）
     */
    private String workAddress;

    /**
     * 注册类型
     */
    private Long registerType;

    /**
     * corp_type 行业分类（sys_param.param_code）
     */
    private Long industryCategory;

    /**
     * 职业病危害风险分类（0-严重、1-较重、2-一般）
     */
    private Integer occupationalRiskClassify;

    /**
     * 在岗职工人数
     */
    private Integer staffCount;

    /**
     * 接害人数（不重复计）
     */
    private Integer pickupCount;

    /**
     * 主营营业收入（万元）
     */
    private BigDecimal mainOperaIncome;

    /**
     * 注册资本
     */
    private BigDecimal registerCapital;

    /**
     * 企业规模
     */
    private String scale;

    /**
     * 企业年度产值
     */
    private BigDecimal yearAssets;

    /**
     * 企业资产总值
     */
    private BigDecimal totalAssets;

    /**
     * 法人代表姓名
     */
    private String legalName;

    /**
     * 法人代表联系电话
     */
    private String legalTel;

    /**
     * 化学事故应急咨询服务电话
     */
    private String chemicalEmergencyPhone;

    /**
     * 职业卫生负责人
     */
    private Long occLeaderSid;

    /**
     * 职业卫生负责人联系电话
     */
    private String occLeaderTel;

    /**
     * 职业卫生管理人员
     */
    private String occManagerName;

    /**
     * ְ管理人员手机号码
     */
    private String occManagerTel;

    /**
     * 企业主要负责人
     */
    private String mainLeaderName;

    /**
     * 负责人手机号码
     */
    private String mainLeaderTel;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 最后修改时间
     */
    private Date updateDate;

    /**
     * 最后修改人ID
     */
    private Long updateBy;

    /**
     * 是否有效（0-无效，1-有效）
     */
    private Boolean isValid;

    /**
     * 数据来源（CompanyDataSourceEnum约定）
     */
    private Integer dataSource;

    /**
     * 数据来源ID（原ID）
     */
    private String dataSourceId;

    /**
     * 企业logo url 地址
     */
    private String logoFileUrl;

    /**
     * [平台字段]企业编号串扩展(社会统一信代码/登记号/组织代码)
     */
    private String corpCcExtend;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 处罚次数
     */
    private Integer punishNum;

    /**
     * 是否有被纳入分类监督企业过(0-否,1-是)
     */
    private Boolean isDec;

    /**
     * 是否是今年分类监督企业(0-否,1-是)
     */
    private Boolean isDecCurrentYear;

    /**
     * 是否申报(0-未申报,1-已申报)
     */
    private Boolean isReport;

    /**
     * 所属地区
     */
    private Long belongToRegion;

    /**
     * 自查结果是否可靠(0-否,1-是)
     */
    private Boolean isReliable;

    /**
     * 是否存在放射性危害因素(0否，1是)
     */
    private Boolean isExistRadioactivity;

    /**
     * 是否是监督对象
     */
    private Boolean isSupervise;

    /**
     * 特殊排序需求
     */
    private Integer specSort;

    /**
     * 优化查询使用-视图company_supervise_view中的结果
     */
    private Boolean isShowSupervise;

    /**
     * 记录去年四个来源的接害人数
     */
    private String lastYearPickUpCount;

}
