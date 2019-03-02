package com.lnsoft.config;

/**
 * Created By Chr on 2019/1/28/0028.
 * <p>
 * 该类用来存放mapper.xml的sql语句的配置信息：如id，resultType和sql语句等
 */
public class MappedStatement {

    //ns
    private String namespace;
    //sql的编号id
    private String sourceId;
    //查询完转换成的java对象，为全限定名
    private String resultType;
    //sql语句
    private String sql;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
