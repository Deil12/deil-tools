package org.deil.utils.datasource.mapper;

import org.deil.utils.datasource.aspect.annotation.DataSource;
import org.deil.utils.datasource.domain.DataSourceEnum;
import org.springframework.stereotype.Repository;

/**
 * @PURPOSE 
 * @DATE 2022/11/24
 */
@Repository
@DataSource(DataSourceEnum.dataSourceSqlServer)
public interface TestMapper/* extends BaseMapperEx<String>*/ {

}
