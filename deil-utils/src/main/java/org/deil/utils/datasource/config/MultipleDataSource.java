package org.deil.utils.datasource.config;

import org.deil.utils.datasource.domain.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @PURPOSE 
 * @DATE 2022/11/24
 */
public class MultipleDataSource extends AbstractRoutingDataSource {

    /**
     * 重写determineCurrentLookupKey()，通过DataSourceContextHolder 获取数据源变量，用于当作lookupKey取出指定的数据源
     * determineCurrentLookupKey，该方法返回一个Object，一般是返回字符串
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }

}
