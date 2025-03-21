package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Report;

public class ReportDto {

    private Long id;
    private String comment;
    private Long reportedId;
    private Long reporterId;

    public ReportDto(Report report) {
        this.id = report.getId();
        this.comment = report.getComment();
        this.reportedId = report.getReported().getId();
        this.reporterId = report.getReporter().getId();
    }
}
