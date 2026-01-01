package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.modules.catalog.dto.PriceListRowDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ExcelReaderService {

    List<PriceListRowDTO> parsePriceList(MultipartFile file) throws IOException;
}
