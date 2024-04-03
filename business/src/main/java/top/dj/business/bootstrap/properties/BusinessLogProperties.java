package top.dj.business.bootstrap.properties;

import lombok.Data;

@Data
public class BusinessLogProperties {
    /**
     * 敏感日志拦截器，启用禁用。true/false
     */
    private Boolean busiLogEnable;

    /**
     * 敏感日志拦截器，线上线下环境标识（互斥）。dev/prod
     */
    private String busiLogOnlineOrOfflineEnv;

    /**
     * 此处记录哪些表字段需拦截记录日志（格式：schema.table.column，逗号分隔），给【库名.表名.字段名】，适配不同的实际执行的SQL。
     */
    private String busiLogRecordKeys;
}
