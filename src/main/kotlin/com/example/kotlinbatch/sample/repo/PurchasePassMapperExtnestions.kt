package com.example.kotlinbatch.sample.repo

import com.example.kotlinbatch.sample.repo.PurchasePassEntitySupport.memberNo
import com.example.kotlinbatch.sample.repo.PurchasePassEntitySupport.prchsId
import com.example.kotlinbatch.sample.repo.PurchasePassEntitySupport.purchasePass
import org.mybatis.dynamic.sql.util.kotlin.SelectCompleter
import org.mybatis.dynamic.sql.util.kotlin.mybatis3.selectList

/**
 *
 */

private val columnList = listOf(prchsId, memberNo)

fun PurchasePassMapper.select(completer: SelectCompleter) =
    selectList(this::selectMany, columnList, purchasePass, completer)
