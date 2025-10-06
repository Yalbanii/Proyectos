package com.proyecto.congreso.pases.batch.listener;

import com.proyecto.congreso.pases.batch.model.BatchJobExecutionLog;
import com.proyecto.congreso.pases.batch.repository.BatchJobExecutionLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "spring.batch.job.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnBean(name = "mongoTemplate")
public class BatchJobExecutionMongoListener implements JobExecutionListener {

    private final BatchJobExecutionLogRepository logRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("🚀 Starting batch job: {} (ID: {})",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId());

        BatchJobExecutionLog executionLog = BatchJobExecutionLog.started(
                jobExecution.getId(),
                jobExecution.getJobInstance().getJobName()
        );

        logRepository.save(executionLog);
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        BatchJobExecutionLog log = logRepository.findByJobExecutionId(jobExecution.getId());

        if (log == null) {
            this.log.warn("No execution log found for job execution: {}", jobExecution.getId());
            return;
        }

        if (jobExecution.getStatus().isUnsuccessful()) {
            String errorMessage = jobExecution.getAllFailureExceptions().stream()
                    .map(Throwable::getMessage)
                    .reduce((a, b) -> a + "; " + b)
                    .orElse("Unknown error");

            log.failed(errorMessage);
            this.log.error("❌ Batch job failed: {} - {}",
                    jobExecution.getJobInstance().getJobName(), errorMessage);
        } else {
            // ✅ CORREGIDO: Usar nombres correctos para este job
            Integer totalAssistances = (Integer) jobExecution.getExecutionContext()
                    .get("totalAssistances");
            Integer successfulAssistances = (Integer) jobExecution.getExecutionContext()
                    .get("successfulAssistances");
            Integer failedAssistances = (Integer) jobExecution.getExecutionContext()
                    .get("failedAssistances");

            // Adaptar el modelo para que sea flexible
            log.completed(
                    totalAssistances != null ? totalAssistances : 0,
                    successfulAssistances != null ? successfulAssistances : 0,
                    failedAssistances != null ? failedAssistances : 0
            );

            this.log.info("✅ Batch job completed: {} (Duration: {}ms, Assistances: {})",
                    jobExecution.getJobInstance().getJobName(),
                    log.getDuration(),
                    totalAssistances);
        }

        logRepository.save(log);
    }
}
