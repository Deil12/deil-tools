package org.deil.utils.datasource;

public enum DataSourceEnum {

    /**
     * 数据库1
     */
    MYSQL("dataSourceMysql"),
    /**
     * 数据库2
     */
    SQLSERVER("dataSourceSqlServer");

    private String value ;

    DataSourceEnum(String value){
        this.value = value ;
    }

    public String getValue() {
        return value;
    }

}
