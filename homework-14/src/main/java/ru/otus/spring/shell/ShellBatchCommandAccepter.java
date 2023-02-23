package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class ShellBatchCommandAccepter {

    private final JobLauncher jobLauncher;
    private final Job migrateMongoToH2;
    private final ApplicationContext appContext;

    @SneakyThrows
    @ShellMethod(value = "StartMigrationFromMongoToH2", key = "start")
    public void startMigrationFromMongoToH2() {
        JobExecution execution = jobLauncher.run(migrateMongoToH2, new JobParametersBuilder().toJobParameters());
        log.info("Execution:", execution);
    }

    @ShellMethod(value = "Exit from application", key = {"exit"})
    public void exit() {
        System.exit(SpringApplication.exit(appContext, () -> 0));
    }

}
