package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.modules.catalog.dto.PriceListRowDTO;
import com.ffrontera._generales.backend.modules.catalog.service.impl.ExcelReaderServiceImpl;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelReaderServiceTest {

    @Test
    void parsePriceList_ShouldReturnRows_WhenFileValid() throws Exception {
        // Crear workbook en memoria
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("prices");
            // header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("SKU");
            header.createCell(1).setCellValue("Desc");
            header.createCell(2).setCellValue("Price");

            Row r1 = sheet.createRow(1);
            r1.createCell(0).setCellValue("SKU-1");
            r1.createCell(1).setCellValue("Product 1");
            r1.createCell(2).setCellValue(123.45);

            Row r2 = sheet.createRow(2);
            r2.createCell(0).setCellValue(""); // empty sku -> ignored
            r2.createCell(1).setCellValue("Product 2");
            r2.createCell(2).setCellValue(10);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            wb.write(bos);
            byte[] content = bos.toByteArray();

            MultipartFile file = new MockMultipartFile("file", "prices.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", content);
            ExcelReaderServiceImpl reader = new ExcelReaderServiceImpl();

            List<PriceListRowDTO> rows = reader.parsePriceList(file);

            assertNotNull(rows);
            assertEquals(1, rows.size());
            PriceListRowDTO row = rows.get(0);
            assertEquals("SKU-1", row.supplierSku());
            assertEquals("Product 1", row.description());
            assertEquals(0, new BigDecimal("123.45").compareTo(row.newCost()));
        }
    }

}