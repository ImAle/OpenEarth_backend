package com.alejandro.OpenEarth.repository;

import com.alejandro.OpenEarth.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("reportRepository")
public interface ReportRepository extends JpaRepository<Report, Long> {

}
