package top.dj.model.businesslog.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public enum RecordOperationTypeEnum {
    // 用人单位信息变更
    BUSI_TYPE_CHANGE((byte) 1, "business_state", "营业状态变更"),
    REGION_CHANGE((byte) 2, "area_id", "行政区划变更"),
    SUP_ORG_CHANGE((byte) 3, "supervision_unit", "监督单位变更"),
    NEW_USER((byte) 4, "", "新增用户"),
    UCC_CHANGE((byte) 5, "unified_social_cc", "信用代码变更"),
    IMPORT_USER((byte) 6, "", "导入用户"),
    COM_NAME_CHANGE((byte) 7, "name", "企业名称变更"),
    VICTIMS_CHANGE1((byte) 100, "", "接害人数变更(实际人数变更)"),
    VICTIMS_CHANGE2((byte) 101, "", "接害人数变更(监管认定)"),
    // OTHER
    ;

    private final Byte key;
    private final String column;
    private final String value;

    public static String getValue(Byte key) {
        if (key == null) {
            return null;
        }
        for (RecordOperationTypeEnum remind : RecordOperationTypeEnum.values()) {
            if (Objects.equals(key, remind.getKey())) {
                return remind.getValue();
            }
        }
        return null;
    }
}
