package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.ReportCreationDto;
import com.alejandro.OpenEarth.dto.ReportDto;
import com.alejandro.OpenEarth.entity.Report;
import com.alejandro.OpenEarth.service.ReportService;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    @Qualifier("reportService")
    private ReportService reportService;

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<?> createReport(@RequestHeader("Authorization") String token, @Valid @RequestBody ReportCreationDto reportDto, BindingResult result) {
        try{
            if(!Objects.equals(jwtService.getUser(token).getId(), reportDto.getReporterId()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You can not report on behalf of someone else");

            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            Report report = reportDto.fromDtoToEntity();
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

        return ResponseEntity.ok().body(Map.of("reports", reports.stream().map(ReportDto::new).toList()));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getReportById(@RequestParam("id") Long id){
        try{
            Report report = reportService.getReportById(id);
            return ResponseEntity.ok().body(Map.of("report", new ReportDto(report)));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }

    }

    @DeleteMapping("/delete")
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
