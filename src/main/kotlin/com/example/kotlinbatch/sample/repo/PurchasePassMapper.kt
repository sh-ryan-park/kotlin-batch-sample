package com.example.kotlinbatch.sample.repo

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Result
import org.apache.ibatis.annotations.ResultMap
import org.apache.ibatis.annotations.Results
import org.apache.ibatis.annotations.SelectProvider
import org.apache.ibatis.type.JdbcType
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider
import org.mybatis.dynamic.sql.util.SqlProviderAdapter
import org.mybatis.dynamic.sql.util.mybatis3.CommonCountMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonDeleteMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonInsertMapper
import org.mybatis.dynamic.sql.util.mybatis3.CommonUpdateMapper

/**
 *
 */
@Mapper
interface PurchasePassMapper : CommonCountMapper, CommonDeleteMapper, CommonInsertMapper<PurchasePassEntity>,
    CommonUpdateMapper {
    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @Results(
        id = "PurchasePassResult",
        value = [
            Result(column = "prchs_id", property = "prchsId", jdbcType = JdbcType.BIGINT, id = true),
            Result(column = "member_no", property = "memberNo", jdbcType = JdbcType.BIGINT),
        ]
    )
    fun selectMany(selectStatement: SelectStatementProvider): List<PurchasePassEntity>

    @SelectProvider(type = SqlProviderAdapter::class, method = "select")
    @ResultMap("PurchasePassResult")
    fun selectOne(selectStatement: SelectStatementProvider): PurchasePassEntity?
}
