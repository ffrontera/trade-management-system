package com.ffrontera._generales.backend.modules.catalog.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PriceUpdateService {

    String processPriceList(Long supplierId, MultipartFile file) throws IOException;
}
