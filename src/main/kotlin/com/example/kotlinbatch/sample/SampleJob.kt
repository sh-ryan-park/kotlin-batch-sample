package com.example.kotlinbatch.sample

import com.example.kotlinbatch.sample.repo.PurchasePassEntity
import com.example.kotlinbatch.sample.repo.PurchasePassEntitySupport.memberNo
import com.example.kotlinbatch.sample.repo.PurchasePassEntitySupport.prchsId
import com.example.kotlinbatch.sample.repo.PurchasePassEntitySupport.purchasePass
import com.example.kotlinbatch.sample.repo.PurchasePassMapper
import com.example.kotlinbatch.sample.repo.select
import org.mybatis.dynamic.sql.util.kotlin.spring.select
import org.mybatis.dynamic.sql.util.kotlin.spring.selectOne
import org.springframework.batch.core.*
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.transaction.PlatformTransactionManager

/**
 *
 */
@Configuration
class SampleJob(
    private val jobRepository: JobRepository,
    private val transactionManager: PlatformTransactionManager,
    private val purchasePassMapper: PurchasePassMapper,
    private val jdbcTemplate: NamedParameterJdbcTemplate,
) {

    @Bean
    fun job(): Job {
        return JobBuilder("sample", jobRepository)
            .start(step())
            .listener(object : JobExecutionListener {
                override fun beforeJob(jobExecution: JobExecution) {
                    println("before job")
                }

                override fun afterJob(jobExecution: JobExecution) {
                    println("after job")
                }
            })
            .build()
    }

    @Bean
    fun step(): Step {
        return StepBuilder("step", jobRepository)
            .tasklet(tasklet(), transactionManager)
            .build()
    }

    @Bean
    fun tasklet(): Tasklet {
        return Tasklet { contribution, chunkContext ->
            println("tasklet started")

            val rows = purchasePassMapper.select {
                where {
                    memberNo isEqualTo 621
                }
            }

            rows.forEach { println("mybatis mapper result $it") }

            val provider = select(prchsId, memberNo) {
                from(purchasePass)
                where { memberNo isEqualTo 621 }
                orderBy(prchsId)
            }

            jdbcTemplate.selectOne(provider) { rs, _ -> PurchasePassEntity(rs.getLong(1), rs.getLong(2)) }
                .also { println("jdbc template result $it") }

            println("tasklet end")
            RepeatStatus.FINISHED
        }
    }

    class SampleTasklet : Tasklet {
        override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {

            println("job run")

            return RepeatStatus.FINISHED
        }
    }
}
