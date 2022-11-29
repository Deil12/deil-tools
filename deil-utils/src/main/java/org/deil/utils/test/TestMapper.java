package org.deil.utils.test;

import org.deil.utils.datasourceHolder.DataSourceEnum;
import org.deil.utils.datasourceHolder.config.DataSource;
import org.springframework.stereotype.Repository;

/**
 * @PURPOSE 
 * @DATE 2022/11/29
 */
//@Mapper
@Repository
@DataSource(DataSourceEnum.SQLSERVER)
public interface TestMapper {

    String test();

}
