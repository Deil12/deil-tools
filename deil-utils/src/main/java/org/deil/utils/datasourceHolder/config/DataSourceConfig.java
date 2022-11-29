package org.deil.utils.datasourceHolder.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.deil.utils.datasourceHolder.DataSourceEnum;
import org.deil.utils.datasourceHolder.MultipleDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @PURPOSE 
 * @DATE 2022/11/28
 */
@Configuration
@MapperScan({"com.csair.exchange.mapper"})
public class DataSourceConfig extends org.apache.ibatis.session.Configuration {

    /**
     * 配置mysql datasource
     * @return
     */
    @Bean(name = "dataSourceMysql")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource dataSourceMysql() {
        return DataSourceBuilder.create().build() ;
    }

    /**
     * 配置 sql server datasource
     * @return
     */
    @Bean(name = "dataSourceSqlServer")
    @ConfigurationProperties(prefix = "spring.datasource.sqlserver")
    public DataSource dataSourceSqlServer() {
        return DataSourceBuilder.create().build() ;
    }

    /**
     * 使用枚举类 映射两个datasource
     * @param mysql
     * @param sqlServer
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("dataSourceMysql") DataSource mysql,
                                         @Qualifier("dataSourceSqlServer") DataSource sqlServer) {
        MultipleDataSource multipleDataSource = new MultipleDataSource() ;
        Map<Object, Object> targetDataSources = new HashMap<>() ;
        targetDataSources.put(DataSourceEnum.MYSQL.getValue(), mysql) ;
        targetDataSources.put(DataSourceEnum.SQLSERVER.getValue(), sqlServer) ;
        //添加数据源
        multipleDataSource.setTargetDataSources(targetDataSources) ;
        //设置默认数据源db1
        multipleDataSource.setDefaultTargetDataSource(sqlServer);
        return multipleDataSource ;
    }

    /*@Bean("sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean() ;
        sqlSessionFactory.setDataSource(multipleDataSource(dataSourceMysql(), dataSourceSqlServer())) ;

        MybatisConfiguration configuration = new MybatisConfiguration() ;
        configuration.setJdbcTypeForNull(JdbcType.NULL) ;
        configuration.setMapUnderscoreToCamelCase(true) ;
        configuration.setCacheEnabled(false) ;
        sqlSessionFactory.setConfiguration(configuration) ;
        sqlSessionFactory.setPlugins(new Interceptor[]{ new PaginationInterceptor() });
        return sqlSessionFactory ;
    }*/

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(/*@Qualifier("dataSourceMysql") DataSource mysql*/) throws Exception {
        DataSourceConfig configuration = new DataSourceConfig();
        configuration.setJdbcTypeForNull(JdbcType.NULL) ;
        configuration.setMapUnderscoreToCamelCase(true) ;
        configuration.setCacheEnabled(false) ;

        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(/*mysql*/multipleDataSource(dataSourceMysql(), dataSourceSqlServer()));
        bean.setConfiguration(configuration);
        bean.setMapperLocations(
                // 设置mybatis的xml所在位置
                new PathMatchingResourcePatternResolver().getResources("classpath*:mybatis-mapping/*.xml"));

        return bean.getObject();
    }

    /*@Bean("sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sessionfactory) {
        return new SqlSessionTemplate(sessionfactory);
    }*/

}
