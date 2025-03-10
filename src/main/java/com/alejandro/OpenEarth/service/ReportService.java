package com.alejandro.OpenEarth.service;

import com.alejandro.OpenEarth.entity.Report;

import java.util.List;

public interface ReportService {
    Report createReport(Report report);
    List<Report> getReports();
    Report getReportById(Long id);
    void deleteReportById(Long id);
}
