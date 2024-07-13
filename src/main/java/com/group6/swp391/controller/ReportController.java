package com.group6.swp391.controller;

import com.group6.swp391.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/swp391/api/report")
@CrossOrigin(origins = "*")
public class ReportController {
    @Autowired private ReportService reportService;

    @GetMapping("/user/{reportFormat}")
    public void getReportUser(@PathVariable String reportFormat, HttpServletResponse response) throws JRException, IOException, SQLException, NoSuchFieldException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=UserExport.html";
        if(reportFormat.equals("pdf")) {
            headerValue = "attachment; filename=UserExport.pdf";
            response.setHeader(headerKey, headerValue);
            reportService.exportReport(response, reportFormat);
        } else if(reportFormat.equals("html")) {
            response.setHeader(headerKey, headerValue);
            reportService.exportReport(response, reportFormat);
        }
    }


    @GetMapping("/user/excel")
    public void generateExcelReport(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=UserExport.xlsx";
        response.setHeader(headerKey, headerValue);
        reportService.exportExcel(response);
    }

}
