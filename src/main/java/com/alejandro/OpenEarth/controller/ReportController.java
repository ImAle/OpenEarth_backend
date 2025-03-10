package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.ReportDto;
import com.alejandro.OpenEarth.entity.Report;
import com.alejandro.OpenEarth.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    @Qualifier("reportService")
    private ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<?> createReport(@RequestBody ReportDto reportDto) {
        try{
            ReportDto dto = new ReportDto();
            Report report = dto.fromDtoToEntity(reportDto);
            reportService.createReport(report);
            String message = report.getReported().getUsername() + "has been successfully reported";

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", message));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", ex.getMessage()));
        }

    }

    @GetMapping("")
    public ResponseEntity<?> getAllReports() {
        List<Report> reports = reportService.getReports();
        if(reports.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(Map.of("reports", reports));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteReport(@RequestParam("id") Long reportId) {
        try{
            reportService.deleteReportById(reportId);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", rtex.getMessage()));
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }
}
