package com.example.kotlinbatch.sample.repo

import org.mybatis.dynamic.sql.SqlTable
import org.mybatis.dynamic.sql.util.kotlin.elements.column
import java.sql.JDBCType

/**
 *
 */
object PurchasePassEntitySupport {
    val purchasePass = PurchasePass()
    val prchsId = purchasePass.prchsId
    val memberNo = purchasePass.memberNo

    class PurchasePass : SqlTable("tb_prchs_pass") {
        val prchsId = column<Long>(name = "prchs_id", jdbcType = JDBCType.BIGINT)
        val memberNo = column<Long>(name = "member_no", jdbcType = JDBCType.BIGINT)
    }
}
