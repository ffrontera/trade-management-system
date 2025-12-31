package com.ffrontera._generales.backend.common.controller;

import com.ffrontera._generales.backend.common.service.StorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MediaController.class)
class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StorageService storageService;

    @Test
    void upload_ShouldReturnUrl_WhenFileIsUploaded() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "foto.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "contenido_dummy".getBytes()
        );

        String expectedUrl = "https://res.cloudinary.com/demo/image/upload/v1/foto.jpg";

        when(storageService.uploadImage(any())).thenReturn(expectedUrl);

        mockMvc.perform(multipart("/api/media/upload").file(file))
                .andExpect(status().isOk()) // Esperamos HTTP 200
                .andExpect(jsonPath("$.url").value(expectedUrl));
    }


}