package top.dj.log.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

import static cn.hutool.core.collection.CollUtil.isNotEmpty;

/**
 * @Author: DengJia
 * @Date: 2024/2/28
 * @Description:
 */

@Slf4j
public class SqlTools {
    private SqlTools() {
    }

    /**
     * 解析BoundSql，生成不含占位符的SQL语句
     *
     * @param configuration MyBatis主配置信息
     * @param boundSql      存放SQL语句的对象
     * @return 不含占位符的SQL语句
     */
    public static String showSql(Configuration configuration, BoundSql boundSql) {
        // 获取参数
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappingList = boundSql.getParameterMappings();

        // 格式化SQL（多个空格都用一个空格代替）
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        boolean hasParams = parameterObject != null && isNotEmpty(parameterMappingList);
        if (hasParams) {
            // 获取类型处理器注册器，类型处理器的功能是进行Java类型和数据库类型的转换
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            // 如果根据parameterObject.getClass()可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                /*
                 * MetaObject主要是封装了originalObject对象，
                 *      提供了get和set的方法用于获取和设置originalObject的属性值，
                 *      主要支持对JavaBean、Collection、Map三种类型对象的操作。
                 */
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappingList) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        // 该分支是动态sql
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        // 打印出缺失，提醒该参数缺失并防止错位
                        sql = sql.replaceFirst("\\?", "缺失");
                    }
                }
            }
        }
        return sql;
    }

    /**
     * 若为字符串或者日期类型，则在参数两边添加''
     *
     * @param obj
     * @return java.lang.String
     */
    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            value = String.valueOf(obj);
        }
        return value;
    }

    /**
     * 获取数据库名称
     *
     * @param configuration MyBatis主配置信息
     * @return 数据库名
     */
    public static String extractDbaseName(Configuration configuration) {
        String schema = "";
        Environment environment = configuration.getEnvironment();
        DataSource dataSource = environment.getDataSource();
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            schema = connection.getCatalog();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SqlTools::获取数据库连接异常:", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    log.error("SqlTools::关闭数据库连接异常:", e);
                }
            }
        }
        return schema;
    }
}
