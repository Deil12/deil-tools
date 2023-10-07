package org.deil.utils.utils;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component
public class TransactionUtil {

    ///**
    // * 事务管理器
    // */
    //private final DataSourceTransactionManager dataSourceTransactionManager;
    //
    //public TransactionUtil(DataSourceTransactionManager dataSourceTransactionManager) {
    //    this.dataSourceTransactionManager = dataSourceTransactionManager;
    //}
    //
    ///**
    // * 事务准备阶段
    // *
    // * @param propagationBehavior 事务传播行为
    // * @param isolationLevel      事务隔离级别
    // * @return 事务状态表现对象
    // */
    //public TransactionStatus begin(int propagationBehavior, int isolationLevel) {
    //    // 事务定义
    //    DefaultTransactionDefinition definition = new DefaultTransactionDefinition(propagationBehavior);
    //    // 事务隔离级别
    //    definition.setIsolationLevel(isolationLevel);
    //    // 事务状态
    //    return dataSourceTransactionManager.getTransaction(definition);
    //}
    //
    ///**
    // * 事务准备阶段
    // *
    // * @param isolationLevel 事务隔离级别
    // * @return 事务状态表现对象
    // */
    //public TransactionStatus begin(int isolationLevel) {
    //    // 事务定义，默认传播行为为 Required
    //    DefaultTransactionDefinition definition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRED);
    //    // 事务隔离级别
    //    definition.setIsolationLevel(isolationLevel);
    //    // 事务状态
    //    return dataSourceTransactionManager.getTransaction(definition);
    //}
    //
    //
    ///**
    // * 事务提交
    // *
    // * @param transactionStatus 事务状态表现对象
    // */
    //public void commit(TransactionStatus transactionStatus) {
    //    dataSourceTransactionManager.commit(transactionStatus);
    //}
    //
    //
    ///**
    // * 事务回滚
    // *
    // * @param transactionStatus 事务状态表现对象
    // */
    //public void rollback(TransactionStatus transactionStatus) {
    //    dataSourceTransactionManager.rollback(transactionStatus);
    //}

}
