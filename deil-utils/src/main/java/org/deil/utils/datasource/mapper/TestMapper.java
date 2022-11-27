package org.deil.utils.datasource.mapper;

import org.deil.utils.datasource.aspect.annotation.DataSource;
import org.deil.utils.datasource.domain.DataSourceEnum;
import org.springframework.stereotype.Repository;

/**
 * @PURPOSE 测试映射器
 * @DATE 2022/11/27
 * @COPYRIGHT © Deil
 */
@Repository
@DataSource(DataSourceEnum.dataSourceSqlServer)
public interface TestMapper/* extends BaseMapperEx<String>*/ {

}
