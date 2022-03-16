package br.com.sw2you.realmeet.controller;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import br.com.sw2you.realmeet.api.facade.AllocationsApi;
import br.com.sw2you.realmeet.api.facade.ReportsApi;
import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.service.AllocationService;
import br.com.sw2you.realmeet.service.ReportCreationService;
import br.com.sw2you.realmeet.util.ResponseEntityUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController implements ReportsApi {

    private final Executor controllerExecutor;
    private final ReportCreationService reportCreationService;

    public ReportController(Executor controllerExecutor, ReportCreationService reportCreationService) {
        this.controllerExecutor = controllerExecutor;
        this.reportCreationService = reportCreationService;
    }

    @Override
    public CompletableFuture<ResponseEntity<Void>> createAllocationReport(
            String apiKey,
            String email,
            LocalDate dateFrom,
            LocalDate dateTo,
            String reportFormat
    ) {
        return runAsync(() -> reportCreationService.createAllocationReport(dateFrom, dateTo, email, reportFormat), controllerExecutor)
                .thenApply(ResponseEntityUtils::noContent);
    }

}