package com.alejandro.OpenEarth.controller;

import com.alejandro.OpenEarth.dto.ReportCreationDto;
import com.alejandro.OpenEarth.dto.ReportDto;
import com.alejandro.OpenEarth.entity.Report;
import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.service.ReportService;
import com.alejandro.OpenEarth.serviceImpl.AuthService;
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
    public ResponseEntity<?> createReport(@RequestHeader("Authorization") String token, @Valid @RequestBody ReportCreationDto report, BindingResult result) {
        try{
            User user = jwtService.getUser(token);

            if(!jwtService.isGuest(token) && !jwtService.isHostess(token))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "You can not report if you are not guest or hostess"));

            if(Objects.equals(user.getId(), report.getReportedId()))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "You can not report yourself"));

            if(result.hasErrors())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());

            Report rep = reportService.fromDtoToEntity(report.getReportedId(), user, report.getComment());
            reportService.createReport(rep);
            String message = rep.getReported().getUsername() + " has been successfully reported";

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", message));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", rtex.getMessage()));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", ex.getMessage()));
        }

    }

    @GetMapping("")
    public ResponseEntity<?> getAllReports(@RequestHeader("Authorization") String token) {

        if(!jwtService.isAdmin(token))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You are not allowed to access this information"));

        List<Report> reports = reportService.getReports();
        if(reports.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok().body(Map.of("reports", reports.stream().map(ReportDto::new).toList()));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getReportById(@RequestHeader("Authorization") String token, @RequestParam("id") Long id){
        try{
            if(!jwtService.isAdmin(token))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You are not allowed to access this information"));

            Report report = reportService.getReportById(id);
            return ResponseEntity.ok().body(Map.of("report", new ReportDto(report)));
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", rtex.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", e.getMessage()));
        }

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReport(@RequestHeader("Authorization") String token, @RequestParam("id") Long reportId) {
        try{
            if(!jwtService.isAdmin(token))
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "You are not allowed to access this information"));

            reportService.deleteReportById(reportId);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException rtex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Error", rtex.getMessage()));
        }catch(Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("Error", ex.getMessage()));
        }
    }
}
