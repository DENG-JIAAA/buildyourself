package top.dj.model.businesslog.domain.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;


@Data
@Table(name = "business_log")
public class BusinessLog implements Serializable {

    @Serial
    private static final long serialVersionUID = 7352050262923756969L;

    /**
     * 操作日志主键id
     */
    @Id
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
     * 日志类型（1UPDATE、2DELETE、3INSERT）
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
     * 时间戳
     */
    private Long timestamp;
}
