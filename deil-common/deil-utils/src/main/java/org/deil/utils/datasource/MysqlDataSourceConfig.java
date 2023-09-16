//package org.deil.utils.datasource;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
////@Configuration
//////@MapperScan(basePackages ="**.dao", sqlSessionTemplateRef  = "mysqlSqlSessionTemplate")
//public class MysqlDataSourceConfig {
//
//    private Logger log = LoggerFactory.getLogger(MysqlDataSourceConfig.class);
//
//    @Bean(name = "mysqlDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.mysql")
//    public DataSource mysqlDataSource() {
//        return new DruidDataSource();
//    }
//
//    @Bean(name = "mysqlSqlSessionFactory")
//    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/mysql/*.xml"));
//        bean.setTypeAliasesPackage("**.common.entity.mybatis");
//        return bean.getObject();
//    }
//
//    @Bean("mysqlTransactionManager")
//    public DataSourceTransactionManager sqlServerTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
//        log.info("\033[0;32mMySQL 连接池初始化\033[0m");
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "mysqlSqlSessionTemplate")
//    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//}
