package com.group6.swp391.controller;

import com.group6.swp391.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

@RestController
@RequestMapping("/swp391/api/report")
@CrossOrigin(origins = "*")
public class ReportController {
    @Autowired private ReportService reportService;

    @GetMapping("/user/{reportFormat}")
    public String getReportUser(@PathVariable String reportFormat) throws JRException, FileNotFoundException, SQLException, NoSuchFieldException {
        String path = "D:\\WorkSpaces\\BE\\JasperReportSWP391";
       return reportService.exportReport(reportFormat, path);
    }
}
