package top.dj.model.businesslog.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BusinessLogResponse implements Serializable {

    private static final long serialVersionUID = -3942550526301947704L;

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
     * 日志类型value（1UPDATE、2DELETE）
     */
    private String logTypeName;

    /**
     * 操作类型id（如：营业状态变更、行政区划变更... 具体请参考：RecordOperationTypeEnum）
     */
    private Byte busiType;

    /**
     * 操作类型名称。
     */
    private String busiTypeName;

    /**
     * 请求方式（POST、PUT）
     */
    private String requestMethod;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /**
     * 操作人ID
     */
    private Long createBy;

    /**
     * 操作人姓名
     */
    private String operator;

}
