package com.csair.exchange.datasourceHolder;
/** 
 * @PURPOSE 
 * @DATE 2022/11/28
 * @COPYRIGHT © csair.com
 */
public enum DataSourceEnum {

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
