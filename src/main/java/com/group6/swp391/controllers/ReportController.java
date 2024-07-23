//package com.group6.swp391.controllers;
//
//import com.group6.swp391.enums.EnumExportFile;
//import com.group6.swp391.services.ReportService;
//import jakarta.servlet.http.HttpServletResponse;
//import net.sf.jasperreports.engine.JRException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.IOException;
//import java.sql.SQLException;
//
//@RestController
//@RequestMapping("/swp391/api/report")
//@CrossOrigin(origins = "*")
//public class ReportController {
//
//    @Autowired private ReportService reportService;
//
//    @PreAuthorize("hasRole('ADMIN') or hasRole('STAFF') ")
//    @GetMapping("/user/{reportFormat}")
//    public void getReportUser(@PathVariable String reportFormat, HttpServletResponse response) throws JRException, IOException, SQLException, NoSuchFieldException {
//        boolean check = EnumExportFile.checkExistExportFile(reportFormat);
//        if(check) {
//            response.setContentType("application/octet-stream");
//            String headerKey = "Content-Disposition";
//            String headerValue = "attachment; filename=UserExport.html";
//            if(reportFormat.equals("pdf")) {
//                headerValue = "attachment; filename=UserExport.pdf";
//            } else if(reportFormat.equals("html")) {
//                headerValue = "attachment; filename=UserExport.html";
//            } else if(reportFormat.equals("excel")) {
//                headerValue = "attachment; filename=UserExport.xlsx";
//            }
//            response.setHeader(headerKey, headerValue);
//            reportService.exportReport(response, reportFormat);
//        }
//    }
//
//
//}
