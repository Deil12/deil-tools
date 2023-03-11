package org.deil.demo.dao;

import org.springframework.stereotype.Repository;

//@Mapper
@Repository
//@DataSource(DataSourceEnum.MYSQL)
public interface TestMapper {

    String test();

}
