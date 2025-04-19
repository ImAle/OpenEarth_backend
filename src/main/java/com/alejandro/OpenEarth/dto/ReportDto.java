package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Report;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReportDto {

    private Long id;
    private String comment;
    private Long reportedId;
    private Long reporterId;
    private LocalDateTime createdAt;

    public ReportDto(Report report) {
        this.id = report.getId();
        this.comment = report.getComment();
        this.reportedId = report.getReported().getId();
        this.reporterId = report.getReporter().getId();
        this.createdAt = report.getCreatedAt();
    }
}
