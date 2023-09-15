package org.deil.utils.datasource;

/** 
 * @PURPOSE 
 * @DATE 2022/11/28
 */
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
