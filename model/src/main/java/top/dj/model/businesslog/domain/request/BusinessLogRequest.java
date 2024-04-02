package top.dj.model.businesslog.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BusinessLogRequest implements Serializable {

    private static final long serialVersionUID = -5155950549041314758L;

    /**
     * 操作日志主键id
     */
    private Long id;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 单位id
     */
    private Long companyId;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 日志标题
     */
    private String logTitle;

    /**
     * 发生变更的字段名
     */
    private String changeField;

    /**
     * 日志类型（1UPDATE、2DELETE）
     */
    private Integer logType;

    /**
     * 操作类型（如：营业状态变更、行政区划变更... 具体请参考：RecordOperationTypeEnum）
     */
    private Byte busiType;

    /**
     * 请求的地址
     */
    private String requestUrl;

    /**
     * 变更前信息
     */
    private String beforeInfo;

    /**
     * 变更后信息
     */
    private String afterInfo;

    /**
     * 请求IP
     */
    private String ip;

    /**
     * 请求处理时长（毫秒）
     */
    private Long handleTime;

    /**
     * 操作时间
     */
    private Date createDate;

    /**
     * 操作人ID
     */
    private Long createBy;

    /**
     * 是否有效（0-无效，1-有效）
     */
    private Boolean isValid;

    /**
     * 关键词，用于查询：企业名称、操作人账号名称、或者后续扩展名称...
     */
    private String keyword;

    /**
     * 业务操作类型列表，查询日志的范围只在这里面进行，见：RecordOperationTypeEnum。
     */
    private Integer type;

    /**
     * 区域ID
     */
    private Long regionId;

    /**
     * 区域等级
     */
    private Byte level;

}
