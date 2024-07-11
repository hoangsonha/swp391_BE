package com.group6.swp391.service;

import com.group6.swp391.model.User;
import com.group6.swp391.repository.UserRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired private UserRepository userRepository;

    public String exportReport(String reportFormat, String path) throws FileNotFoundException, JRException, SQLException, NoSuchFieldException {
//        String path = "D:\\WorkSpaces\\BE\\JasperReportSWP391";

        List<User> listsUser = userRepository.findAll();
        if(listsUser.size() > 0) {
            for(User user : listsUser) {
                if(user.getRole() != null) {
                    user.setRoleName(user.getRole().getRoleName().name());
                }
            }
        }

        File file = ResourceUtils.getFile("classpath:user.jrxml");
        JasperReport jr = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listsUser);

        Map<String, Object> hm = new HashMap<>();
        hm.put("Create By", "HoangSonHa");
        JasperPrint jp = JasperFillManager.fillReport(jr, hm, ds);
        if(reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jp, path + "\\user.html");
        }
        if(reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jp, path + "\\user.pdf");
        }
        return "File Create At Path : " + path;
    }
}
