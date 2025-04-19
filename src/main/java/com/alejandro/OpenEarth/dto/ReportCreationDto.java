package com.alejandro.OpenEarth.dto;

import com.alejandro.OpenEarth.entity.Report;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ReportCreationDto {

    @NotBlank
    @Size(min = 10, max = 1000)
    private String comment;
    @NotNull
    private Long reportedId;

    public ReportCreationDto(Report report) {
        this.comment = report.getComment();
        this.reportedId = report.getReported().getId();
    }
}
