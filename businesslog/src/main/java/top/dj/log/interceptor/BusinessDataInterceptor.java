package top.dj.log.interceptor;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.update.UpdateSet;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import top.dj.model.businesslog.domain.entity.BusinessLog;
import top.dj.model.businesslog.enums.RecordOperationTypeEnum;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.hutool.core.util.StrUtil.*;
import static top.dj.model.businesslog.enums.RecordOperationTypeEnum.*;


/**
 * @description: MyBatis - 业务数据变更拦截器，记录数据的变化情况。（https://blog.csdn.net/wind_chasing_boy/article/details/130230335）
 * @version: 1.0
 * @date: 2024/3/9 10:55
 * @param: null
 * @return: {@link null}
 * @author: DengJia 18402894534@163.com
 */
@Slf4j
@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class BusinessDataInterceptor implements Interceptor {
    private static final String PROD = "prod";
    private static final String COMPANY = "company";
    private static final String LOG_SCHEMA_TABLE = "db.business_log";
    private static final String LOG_SCHEMA_TABLE_PROD = "db.business_log";
    private static final Map<String, String> DEV_PROD_DB_MAP;
    private static final String DEV_URL = "jdbc:mysql://127.0.0.1:3306/db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&allowMultiQueries=true";
    private static final String DEV_USERNAME = "root";
    private static final String DEV_PASSWORD = "123456";
    private static final String PROD_URL = "??";
    private static final String PROD_USERNAME = "??";
    private static final String PROD_PASSWORD = "??";

    static {
        // DEV_PROD_DB_MAP，K/V存放：开发/生产 环境的库名映射（应对开发/生产环境 库名不一致的情况）。
        Map<String, String> dbMap = new HashMap<>();
        dbMap.put("db", "db");
        DEV_PROD_DB_MAP = dbMap;
    }

    private String env;
    private boolean enable;
    private Set<String> recordKeySet;
    private Set<String> recordTableSet;
    private JdbcTemplate jdbcTemplate;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (enable) {
            doLog(invocation);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 配置记录日志表
        this.enable = Boolean.parseBoolean(properties.getProperty("enable"));
        this.env = properties.getProperty("env");
        this.recordKeySet = getRecordKeySet(properties.getProperty("recordKeys"));
        this.recordTableSet = getRecordTableSet(this.recordKeySet);

        // 配置 JdbcTemplate
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(DEV_URL);
        dataSource.setUsername(DEV_USERNAME);
        dataSource.setPassword(DEV_PASSWORD);
        if (StringUtils.isNotBlank(env) && PROD.equals(env)) {
            dataSource.setUrl(PROD_URL);
            dataSource.setUsername(PROD_USERNAME);
            dataSource.setPassword(PROD_PASSWORD);
        }
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * 记录敏感数据操作日志
     *
     * @param invocation 拦截参数
     */
    private void doLog(Invocation invocation) {
        boolean skip = Boolean.FALSE;
        // skip = "true".equals(RpcContext.getContext().getAttachments().get("skip_sensitive_log"));
        if (skip) {
            return;
        }
        // 获取方法参数
        final long startTime = System.currentTimeMillis();
        Object[] args = invocation.getArgs();
        Object parameter = args.length > 1 ? args[1] : null;
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Configuration configuration = mappedStatement.getConfiguration();
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        try {
            // 判断是否记日志
            String tName = extractTableName(boundSql.getSql());
            boolean pass = recordTableSet.contains(tName);
            if (pass) {
                String sql = SqlTools.showSql(configuration, boundSql);
                Statement statement = CCJSqlParserUtil.parse(sql);
                List<BusinessLog> logList = new ArrayList<>();
                if (statement instanceof Update update) {
                    List<String> updateEntryList = new ArrayList<>();
                    Map<String, Object> updateColumnMap = new HashMap<>();
                    for (UpdateSet updateSet : update.getUpdateSets()) {
                        List<Column> columnList = updateSet.getColumns();
                        List<Expression> expressionList = updateSet.getExpressions();
                        for (int i = 0; i < columnList.size(); i++) {
                            String key = tName + "." + columnList.get(i).getColumnName();
                            updateEntryList.add(key);
                            updateColumnMap.put(key, String.valueOf(expressionList.get(i)));
                        }
                    }

                    // 判断是否属于需要记录的字段
                    Set<String> entrySet = updateEntryList.stream()
                            .filter(recordKeySet::contains)
                            .collect(Collectors.toSet());

                    // 需要记录日志
                    if (CollUtil.isNotEmpty(entrySet)) {
                        // 先查变更前数据
                        PlainSelect selectBefore = new PlainSelect();
                        List<SelectItem> selectItemList = new ArrayList<>();
                        selectItemList.add(new SelectExpressionItem(new Column("*")));
                        selectBefore.setSelectItems(selectItemList);
                        selectBefore.setFromItem(new Table(tName));
                        selectBefore.setWhere(update.getWhere());

                        long current = System.currentTimeMillis();
                        List<Map<String, Object>> beforeMapList = jdbcTemplate.queryForList(selectBefore.toString());
                        for (Map<String, Object> beforeMap : beforeMapList) {
                            // 每一个发生变更的企业都需要记录日志
                            for (String entry : entrySet) {
                                String column = entry.split("\\.")[1];
                                String beforeStr = String.valueOf(beforeMap.get(column)).trim();
                                String afterStr = String.valueOf(updateColumnMap.get(entry)).trim();
                                // 去除 单/双引号
                                String before = beforeStr.replace("'", "").replace("\"", "");
                                String after = afterStr.replace("'", "").replace("\"", "");
                                // 关键字段前后不同 记日志
                                if (!Objects.equals(before, after)) {
                                    long cid = COMPANY.equals(tName)
                                            ? Convert.toLong(beforeMap.get("id"), Convert.toLong(beforeMap.get("ID")))
                                            : Convert.toLong(beforeMap.get("cid"), Convert.toLong(beforeMap.get("CID")));

                                    log.info("敏感日志拦截，记录操作变更信息::{}::{}::{}::{}->{}", tName, cid, column, before, after);
                                    // String ip = RpcContext.getContext().getAttachments().get("ip_addr");
                                    // String op = RpcContext.getContext().getAttachments().get("operator_id");
                                    BusinessLog log = new BusinessLog();
                                    log.setId(IdWorker.getId());
                                    log.setTableName(tName);
                                    log.setCompanyId(cid);
                                    log.setLogTitle("由 " + before + " 变更为 " + after);
                                    log.setChangeField(column);
                                    log.setLogType(1);
                                    Byte busiType = Arrays.stream(values())
                                            .filter(enu -> enu.getColumn().equalsIgnoreCase(column))
                                            .map(RecordOperationTypeEnum::getKey)
                                            .findFirst().orElse(null);
                                    log.setBusiType(busiType);
                                    log.setBeforeInfo(before);
                                    log.setAfterInfo(after);
                                    log.setIp(null);
                                    log.setCreateBy(Convert.toLong(null));
                                    log.setTimestamp(current);
                                    logList.add(log);
                                }
                            }
                        }
                    }
                }
                if (statement instanceof Insert insert) {
                    if (COMPANY.equals(tName)) {
                        // String ip = RpcContext.getContext().getAttachments().get("ip_addr");
                        // String op = RpcContext.getContext().getAttachments().get("operator_id");
                        // 获取插入的列名和对应的值 存入 Map 中
                        List<Map<String, Object>> insertMapList = new ArrayList<>();
                        List<Column> columnList = insert.getColumns();
                        ItemsList itemList = insert.getItemsList();
                        ExpressionList expressionListBean = (ExpressionList) itemList;
                        if (expressionListBean.isUsingBrackets()) {
                            // 单条
                            buildInsertMapList(insertMapList, columnList, expressionListBean);
                        } else {
                            // 批量
                            expressionListBean.getExpressions().forEach(expression ->
                                    buildInsertMapList(insertMapList, columnList, ((RowConstructor) expression).getExprList()));
                        }
                        long current = System.currentTimeMillis();
                        Byte busiType = getBusiType(parameter);
                        for (Map<String, Object> insertMap : insertMapList) {
                            Long cid = Convert.toLong(insertMap.get("id"), Convert.toLong(insertMap.get("ID")));
                            String value = getValue(busiType);
                            log.info("敏感日志拦截，数据表新增行::{}::{}::{}", tName, cid, value);
                            BusinessLog log = new BusinessLog();
                            log.setId(IdWorker.getId());
                            log.setTableName(tName);
                            log.setCompanyId(cid);
                            log.setLogTitle(value);
                            log.setLogType(3);
                            log.setBusiType(busiType);
                            log.setIp(null);
                            log.setCreateBy(Convert.toLong(null));
                            log.setTimestamp(current);
                            logList.add(log);
                        }
                    }
                }
                if (CollUtil.isNotEmpty(logList)) {
                    logList.forEach(l -> l.setHandleTime(System.currentTimeMillis() - startTime));
                    List<String> insertSqlList = buildInsertSql(logList, PROD.equals(env) ? LOG_SCHEMA_TABLE_PROD : LOG_SCHEMA_TABLE);
                    // 存日志
                    jdbcTemplate.batchUpdate(insertSqlList.toArray(new String[0]));
                }
            }
        } catch (Exception e) {
            log.error("记录敏感日志异常::", e);
        }
    }

    /**
     * 获取记录key集合（key：xx_table.xx_column）
     *
     * @param recordKeys xx_dbase.xx_table.xx_column 字符串,分隔
     * @return key集合
     */
    private Set<String> getRecordKeySet(String recordKeys) {
        Set<String> keySet = new HashSet<>();
        String[] keyArray = recordKeys.split("\\s*,\\s*");
        Map<String, String> dbMap = PROD.equals(env)
                ? DEV_PROD_DB_MAP
                : MapUtil.reverse(DEV_PROD_DB_MAP);
        for (String key : keyArray) {
            String[] partArray = key.split("\\.");
            String db1 = partArray[0];
            String db2 = dbMap.getOrDefault(db1, db1); // 如果找不到对应的值，使用原始的key
            StringBuilder newKey = new StringBuilder(db2);
            for (int i = 1; i < partArray.length; i++) {
                newKey.append(".").append(partArray[i]);
            }
            keySet.add(newKey.toString());
        }
        return keySet.stream()
                .map(t -> t.substring(t.indexOf(".") + 1).toLowerCase())
                .collect(Collectors.toSet());
    }

    /**
     * 获取表集合
     *
     * @param recordKeySet key集合
     * @return 表集合
     */
    private Set<String> getRecordTableSet(Set<String> recordKeySet) {
        return CollUtil.isEmpty(recordKeySet)
                ? Collections.emptySet()
                : recordKeySet.stream()
                .map(t -> t.substring(0, t.indexOf(".")))
                .collect(Collectors.toSet());
    }

    /**
     * 提取表名
     *
     * @param sql sql语句
     * @return 表名
     */
    public static String extractTableName(String sql) {
        String tableName = null;
        Pattern pattern = Pattern.compile("\\b(?i)(?:insert\\s+into|delete\\s+from|update|merge\\s+into)\\s+((?:\\w+\\.)*\\w+)");
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            tableName = matcher.group(1);
        }
        if (isNotBlank(tableName) && tableName.contains(".")) {
            tableName = tableName.substring(tableName.indexOf(".") + 1);
        }
        return tableName;
    }

    /**
     * 构建插入对象列表
     *
     * @param insertMapList      插入对象列表
     * @param columnList         列名列表
     * @param expressionListBean 值列表
     */
    private void buildInsertMapList(List<Map<String, Object>> insertMapList, List<Column> columnList, ExpressionList expressionListBean) {
        List<Expression> expressionList = expressionListBean.getExpressions();
        Map<String, Object> insertMap = new HashMap<>();
        for (int i = 0; i < columnList.size(); i++) {
            Column column = columnList.get(i);
            Expression expression = expressionList.get(i);
            insertMap.put(column.getColumnName(), expression.toString());
        }
        insertMapList.add(insertMap);
    }

    /**
     * 添加企业方式（1新增2导入）
     *
     * @param parameter param
     * @return 添加方式
     */
    private static Byte getBusiType(Object parameter) {
        // 业务类型
        Byte busiType = null;
        Map<String, Object> parameterMap = BeanUtil.beanToMap(parameter);
        if (MapUtil.isNotEmpty(parameterMap)) {
            Object insertCompanyType = parameterMap.get("insertCompanyType");
            if (insertCompanyType != null) {
                if ("1".equals(insertCompanyType.toString())) {
                    busiType = NEW_USER.getKey();
                }
                if ("2".equals(insertCompanyType.toString())) {
                    busiType = IMPORT_USER.getKey();
                }
            }
        }
        return busiType;
    }

    /**
     * 构建INSERT语句
     *
     * @param logList 日志列表
     * @param tName   表名
     * @return INSERT语句列表
     */
    public static List<String> buildInsertSql(List<BusinessLog> logList, String tName) {
        List<String> sqlList = new ArrayList<>();
        try {
            Table table = new Table();
            table.setName(tName);
            for (BusinessLog log : logList) {
                Insert insert = new Insert();
                insert.setTable(table);

                // 设置要插入的列
                List<Column> columns = Arrays.asList(
                        new Column("id"),
                        new Column("table_name"),
                        new Column("company_id"),
                        new Column("log_title"),
                        new Column("change_field"),
                        new Column("log_type"),
                        new Column("busi_type"),
                        new Column("before_info"),
                        new Column("after_info"),
                        new Column("ip"),
                        new Column("create_by"),
                        new Column("timestamp")
                );
                insert.addColumns(columns);

                // 设置要插入的值
                ExpressionList expressionListBean = new ExpressionList();
                List<Expression> expressionList = new ArrayList<>();
                expressionList.add(new LongValue(log.getId()));
                expressionList.add(log.getTableName() == null ? null : new StringValue(log.getTableName()));
                expressionList.add(log.getCompanyId() == null ? null : new LongValue(log.getCompanyId()));
                expressionList.add(log.getLogTitle() == null ? null : new StringValue(log.getLogTitle()));
                expressionList.add(log.getChangeField() == null ? null : new StringValue(log.getChangeField()));
                expressionList.add(log.getLogType() == null ? null : new LongValue(log.getLogType()));
                expressionList.add(log.getBusiType() == null ? null : new LongValue(log.getBusiType()));
                expressionList.add(log.getBeforeInfo() == null ? null : new StringValue(log.getBeforeInfo()));
                expressionList.add(log.getAfterInfo() == null ? null : new StringValue(log.getAfterInfo()));
                expressionList.add(log.getIp() == null ? null : new StringValue(log.getIp()));
                expressionList.add(log.getCreateBy() == null ? null : new LongValue(log.getCreateBy()));
                expressionList.add(new LongValue(log.getTimestamp()));
                expressionListBean.setExpressions(expressionList);
                insert.setItemsList(expressionListBean);

                // insert sql
                sqlList.add(String.valueOf(insert));
            }
        } catch (Exception e) {
            log.error("构建INSERT语句失败", e);
        }
        return sqlList;
    }
}
