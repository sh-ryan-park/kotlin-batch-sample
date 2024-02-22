package com.example.kotlinbatch.config

import org.mybatis.dynamic.sql.util.spring.NamedParameterJdbcTemplateExtensions
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import javax.sql.DataSource

/**
 *
 */
@Configuration
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    fun mysqlDataSource(): DataSource = DataSourceBuilder.create().build()

    @Bean
    @Primary
    fun transactionManager() = DataSourceTransactionManager(mysqlDataSource())

    @Bean
    fun template() = NamedParameterJdbcTemplate(mysqlDataSource())

    @Bean
    fun templateExtensions() = NamedParameterJdbcTemplateExtensions(template())
}
