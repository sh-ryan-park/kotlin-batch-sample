package com.example.kotlinbatch.config

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.explore.JobExplorer
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.boot.autoconfigure.batch.BatchDataSourceScriptDatabaseInitializer
import org.springframework.boot.autoconfigure.batch.BatchProperties
import org.springframework.boot.autoconfigure.batch.JobLauncherApplicationRunner
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.support.JdbcTransactionManager


/**
 *
 */
@Configuration
@EnableBatchProcessing(dataSourceRef = "batchDataSource", transactionManagerRef = "batchTransactionManager")
@EnableConfigurationProperties(BatchProperties::class)
class BatchConfig {

    @Bean
    fun batchDataSource() =
        EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("/org/springframework/batch/core/schema-h2.sql")
            .build()

    @Bean
    fun batchTransactionManager() = JdbcTransactionManager(batchDataSource())

    @Bean
    fun jobLauncher(jobRepository: JobRepository) = TaskExecutorJobLauncher().also {
        it.setJobRepository(jobRepository)
        it.afterPropertiesSet()
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.batch.job", name = ["enabled"], havingValue = "true", matchIfMissing = true)
    fun jobLauncherApplicationRunner(
        jobLauncher: JobLauncher,
        jobExplorer: JobExplorer,
        jobRepository: JobRepository,
        properties: BatchProperties
    ): JobLauncherApplicationRunner {
        val runner = JobLauncherApplicationRunner(jobLauncher, jobExplorer, jobRepository)
        val jobNames = properties.job.name
        if (jobNames.isNotBlank()) {
            runner.setJobName(jobNames)
        }
        return runner
    }

    @Bean
    @ConditionalOnMissingBean(BatchDataSourceScriptDatabaseInitializer::class)
    fun batchDataSourceInitializer(
        properties: BatchProperties
    ): BatchDataSourceScriptDatabaseInitializer {
        return BatchDataSourceScriptDatabaseInitializer(
            batchDataSource(),
            properties.jdbc
        )
    }
}
