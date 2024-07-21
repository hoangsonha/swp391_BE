package com.group6.swp391.services;

import com.group6.swp391.pojos.User;
import com.group6.swp391.repositories.UserRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import com.group6.swp391.enums.EnumExportFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired private UserRepository userRepository;

    @Value("${excel.sheetName}")
    private String excelTitle;

    @Value("${excel.startRow}")
    private int startRow;

    @Value("${excel.startRowData}")
    private int startRowData;

    @Value("${report.createBy}")
    private String createBy;

    public void exportReport(HttpServletResponse response,String reportFormat) throws IOException, JRException, SQLException, NoSuchFieldException {

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
        hm.put("Create By", createBy);
        JasperPrint jp = JasperFillManager.fillReport(jr, hm, ds);

        ServletOutputStream ops =  response.getOutputStream();

        if (reportFormat.equalsIgnoreCase(EnumExportFile.HTML.name())) {

            File tempHtmlFile = File.createTempFile("user", ".html");
            JasperExportManager.exportReportToHtmlFile(jp, tempHtmlFile.getAbsolutePath());

            // Đọc nội dung tệp HTML tạm thời và ghi vào output stream
            FileInputStream fis = new FileInputStream(tempHtmlFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                ops.write(buffer, 0, bytesRead);
            }
            fis.close();
            tempHtmlFile.delete();

        } else if (reportFormat.equalsIgnoreCase(EnumExportFile.PDF.name())) {
            JasperExportManager.exportReportToPdfStream(jp, ops);

        } else if(reportFormat.equalsIgnoreCase(EnumExportFile.EXCEL.name())) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(excelTitle);
            XSSFRow row = sheet.createRow(startRow);

            row.createCell(0).setCellValue("userID");
            row.createCell(1).setCellValue("firstName");
            row.createCell(2).setCellValue("lastName");
            row.createCell(3).setCellValue("email");
            row.createCell(4).setCellValue("phone");
            row.createCell(5).setCellValue("address");
            row.createCell(6).setCellValue("enabled");
            row.createCell(7).setCellValue("non-Locked");

            // data start from row 2 because row 1 is title that set above
            int dataRowIndex = startRowData;

            for(User user : listsUser) {
                XSSFRow dataRow = sheet.createRow(dataRowIndex);
                dataRow.createCell(0).setCellValue(user.getUserID());
                dataRow.createCell(1).setCellValue(user.getFirstName());
                dataRow.createCell(2).setCellValue(user.getLastName());
                dataRow.createCell(3).setCellValue(user.getEmail());
                dataRow.createCell(4).setCellValue(user.getPhone());
                dataRow.createCell(5).setCellValue(user.getAddress());
                dataRow.createCell(6).setCellValue(user.isEnabled());
                dataRow.createCell(7).setCellValue(user.isNonLocked());
                dataRowIndex++;
            }

            // send data to client (can send any file binary such as picture, video, ...)

            workbook.write(ops);
            workbook.close();
        }
        ops.close();
    }

//    public void exportExcel(HttpServletResponse response) throws IOException {
//
//        List<User> userList = userRepository.findAll();
//        XSSFWorkbook workbook = new XSSFWorkbook();
//        XSSFSheet sheet = workbook.createSheet("User Infor");
//        XSSFRow row = sheet.createRow(0);
//
//        row.createCell(0).setCellValue("userID");
//        row.createCell(1).setCellValue("firstName");
//        row.createCell(2).setCellValue("lastName");
//        row.createCell(3).setCellValue("email");
//        row.createCell(4).setCellValue("phone");
//        row.createCell(5).setCellValue("address");
//        row.createCell(6).setCellValue("enabled");
//        row.createCell(7).setCellValue("non-Locked");
//
//        // data start from row 2 because row 1 is title that set above
//        int dataRowIndex = 1;
//
//        for(User user : userList) {
//            XSSFRow dataRow = sheet.createRow(dataRowIndex);
//            dataRow.createCell(0).setCellValue(user.getUserID());
//            dataRow.createCell(1).setCellValue(user.getFirstName());
//            dataRow.createCell(2).setCellValue(user.getLastName());
//            dataRow.createCell(3).setCellValue(user.getEmail());
//            dataRow.createCell(4).setCellValue(user.getPhone());
//            dataRow.createCell(5).setCellValue(user.getAddress());
//            dataRow.createCell(6).setCellValue(user.isEnabled());
//            dataRow.createCell(7).setCellValue(user.isNonLocked());
//            dataRowIndex++;
//        }
//
//        // send data to client (can send any file binary such as picture, video, ...)
//
//        ServletOutputStream ops =  response.getOutputStream();
//        workbook.write(ops);
//        workbook.close();
//        ops.close();
//    }
}
