package com.ffrontera._generales.backend.modules.catalog.service.impl;

import com.ffrontera._generales.backend.modules.catalog.dto.PriceListRowDTO;
import com.ffrontera._generales.backend.modules.catalog.service.ExcelReaderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class ExcelReaderServiceImpl implements ExcelReaderService {
    @Override
    public List<PriceListRowDTO> parsePriceList(MultipartFile file) throws IOException {
        List<PriceListRowDTO> rows = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }

                try {
                    String sku = getCellValueAsString(row.getCell(0));
                    String desc = getCellValueAsString(row.getCell(1));
                    BigDecimal price = getBigDecimalFromCell(row.getCell(2));

                   if (!sku.isEmpty() && price.compareTo(BigDecimal.ZERO) > 0) {
                       rows.add(new PriceListRowDTO(
                               sku,
                               desc,
                               price
                       ));
                   }
                } catch (Exception e) {
                    log.warn("Error parsing row {}: {}", rowIndex, e.getMessage());
                }
                rowIndex++;
            }
        }
        return rows;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }

    private BigDecimal getBigDecimalFromCell(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;
        if (cell.getCellType() == CellType.NUMERIC)
            return BigDecimal.valueOf(cell.getNumericCellValue());
        return BigDecimal.ZERO;
    }
}
