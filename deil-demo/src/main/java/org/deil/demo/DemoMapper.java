package org.deil.demo;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
//@Repository
//@DataSource(DataSourceEnum.MYSQL)
public interface DemoMapper {

    String test();

}
