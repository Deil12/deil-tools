//package org.deil.utils.datasource;
//
//import com.alibaba.druid.pool.DruidDataSource;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.core.io.support.ResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//@MapperScan(basePackages ="**.dao", sqlSessionTemplateRef  = "sqlServerSqlSessionTemplate")
//public class SqlServerDataSourceConfig {
//    private Logger log = LoggerFactory.getLogger(SqlServerDataSourceConfig.class);
//
//    @Primary
//    @Bean(name = "sqlServerDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
//    public DataSource sqlServerDataSource() {
//        return new DruidDataSource();
//    }
//
//    @Primary
//    @Bean(name = "sqlServerSqlSessionFactory")
//    public SqlSessionFactory sqlServerSqlSessionFactory(@Qualifier("sqlServerDataSource") DataSource dataSource) throws Exception {
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        List<String> pathList = new ArrayList<>();
//        pathList.add("classpath*:mappers/sqlserver/*.xml");
//        List<Resource> resourceList = new ArrayList<>();
//        for (String path : pathList) {
//            Resource[] resources = resolver.getResources(path);
//            resourceList.addAll(Arrays.asList(resources));
//        }
//        Resource[] mapperLocations = resourceList.toArray(new Resource[resourceList.size()]);
//
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//        bean.setDataSource(dataSource);
//        bean.setMapperLocations(mapperLocations);
//        bean.setTypeAliasesPackage("**.common.entity.mybatis");
//        return bean.getObject();
//    }
//
//    @Bean("sqlServerTransactionManager")
//    @Primary
//    public DataSourceTransactionManager sqlServerTransactionManager(@Qualifier("sqlServerDataSource") DataSource dataSource) {
//        log.info("\033[0;32mMsSQL 连接池初始化\033[0m");
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Bean(name = "sqlServerSqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate sqlServerSqlSessionTemplate(@Qualifier("sqlServerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//
//}
