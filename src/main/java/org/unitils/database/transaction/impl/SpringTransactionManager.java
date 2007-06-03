/*
 * Copyright 2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.unitils.database.transaction.impl;

import javax.sql.DataSource;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.unitils.core.Unitils;
import org.unitils.core.UnitilsException;
import org.unitils.database.transaction.TransactionManager;
import org.unitils.spring.SpringModule;

/**
 * Transaction manager that relies on Spring transaction management. When starting a Transaction, this transaction
 * manager tries to locate a configured <code>org.springframework.transaction.PlatformTransactionManager</code> bean
 * instance in the spring ApplicationContext configured in the {@link SpringModule} for this testObject. If no such
 * bean was configured for this test, a <code>org.springframework.jdbc.datasource.DataSourceTransactionManager</code>
 * instance is created.
 *
 * @author Filip Neven
 * @author Tim Ducheyne
 */
public class SpringTransactionManager implements TransactionManager {

    /**
     * ThreadLocal for holding the TransactionStatus as used by spring's transaction management
     */
    private ThreadLocal<TransactionStatus> transactionStatusHolder = new ThreadLocal<TransactionStatus>();


    /**
     * Initializes the transaction manager with the given {@link DataSource}.
     *
     * @param dataSource The data source, not null
     */
    public void setDataSource(DataSource dataSource) {
    }


    /**
     * Starts the transaction. Will start a transaction on the PlatformTransactionManager that is configured
     * in the spring application context associated with the given testObject.
     *
     * @param testObject The test object, not null
     */
    public void startTransaction(Object testObject) {
        PlatformTransactionManager springTransactionManager = getSpringTransactionManager(testObject);
        TransactionStatus transactionStatus = springTransactionManager.getTransaction(createTransactionDefinition(testObject));
        transactionStatusHolder.set(transactionStatus);
    }


    /**
     * Commits the transaction. Will commit on the PlatformTransactionManager that is configured
     * in the spring application context associated with the given testObject.
     *
     * @param testObject The test object, not null
     */
    public void commit(Object testObject) {
        TransactionStatus transactionStatus = transactionStatusHolder.get();
        if (transactionStatus == null) {
            throw new UnitilsException("Trying to commit, while no transaction is currently active");
        }
        getSpringTransactionManager(testObject).commit(transactionStatus);
        transactionStatusHolder.remove();
    }


    /**
     * Rolls back the transaction. Will rollback on the PlatformTransactionManager that is configured
     * in the spring application context associated with the given testObject.
     *
     * @param testObject The test object, not null
     */
    public void rollback(Object testObject) {
        TransactionStatus transactionStatus = transactionStatusHolder.get();
        if (transactionStatus == null) {
            throw new UnitilsException("Trying to rollback, while no transaction is currently active");
        }
        getSpringTransactionManager(testObject).rollback(transactionStatus);
        transactionStatusHolder.remove();
    }


    /**
     * Returns a <code>TransactionDefinition</code> object containing the necessary transaction parameters. Simply
     * returns a default <code>DefaultTransactionDefinition</code> object without specifying any custom properties on
     * it.
     *
     * @param testObject The test object, not null
     * @return The default TransactionDefinition
     */
    protected TransactionDefinition createTransactionDefinition(Object testObject) {
        return new DefaultTransactionDefinition();
    }


    /**
     * @param testObject The test object, not null
     * @return The <code>PlatformTransactionManager</code> that is configured in the spring application context associated with the given testObject.
     * @throws UnitilsException If no <code>PlatformTransactionManager</code> was configured for the given test object
     */
    protected PlatformTransactionManager getSpringTransactionManager(Object testObject) {
        return (PlatformTransactionManager) getSpringModule().getSpringBeanByType(testObject, PlatformTransactionManager.class);
    }


    /**
     * @return The Spring module
     */
    protected SpringModule getSpringModule() {
        return Unitils.getInstance().getModulesRepository().getModuleOfType(SpringModule.class);
    }

}