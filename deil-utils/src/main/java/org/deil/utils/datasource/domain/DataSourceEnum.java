package org.deil.utils.datasource.domain;

/**
 * @PURPOSE 数据源枚举
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
public enum  DataSourceEnum {

    /**
     * 数据库1
     */
    dataSourceMysql("dataSourceMysql"),
    /**
     * 数据库2
     */
    dataSourceSqlServer("dataSourceSqlServer");

    private String value ;

    DataSourceEnum(String value){
        this.value = value ;
    }

    public String getValue() {
        return value;
    }

}
