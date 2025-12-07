package com.example.lorryManagement.controller;

import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.service.LorryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LorryController.class)
public class LorryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LorryService lorryService;

    @Test
    void addLorry_whenValid_shouldReturn201AndBody() throws Exception {
        LorryRequestDto requestDto = new LorryRequestDto();
        requestDto.setLr(1111L);
        requestDto.setLorryNumber("MH12AB1234");
        requestDto.setDate(LocalDate.now());
        requestDto.setWeight(BigDecimal.valueOf(100));
        requestDto.setConsignorName("Rashmi");
        requestDto.setToLocation("Pune");
        requestDto.setFromLocation("Nagpur");

        LorryEntity savedEntity = new LorryEntity();
        savedEntity.setLr(1111L);
        savedEntity.setLorryNumber("MH12AB1234");
        savedEntity.setDate(LocalDate.now());
        savedEntity.setWeight(BigDecimal.valueOf(100));
        savedEntity.setConsignorName("Rashmi");
        savedEntity.setToLocation("Pune");
        savedEntity.setFromLocation("Nagpur");

        when(lorryService.save(any(LorryEntity.class))).thenReturn(savedEntity);
        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/lorry").contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/lorry/1111"))
                .andExpect(jsonPath("$.lr").value(1111))
                .andExpect(jsonPath("$.lorryNumber").value("MH12AB1234"));

        verify(lorryService).save(any(LorryEntity.class));
    }

    @Test
    void addLorry_whenDuplicateLr_shouldReturn409() throws Exception {
        LorryRequestDto requestDto = new LorryRequestDto();
        requestDto.setLr(1111L);
        requestDto.setLorryNumber("MH12AB1234");
        requestDto.setDate(LocalDate.now());
        requestDto.setWeight(BigDecimal.valueOf(100));
        requestDto.setConsignorName("Rashmi");
        requestDto.setToLocation("Pune");
        requestDto.setFromLocation("Nagpur");

        when(lorryService.save(any(LorryEntity.class))).thenThrow(new DuplicateKeyException("LR 1111 already exists"));

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/lorry").contentType(MediaType.APPLICATION_JSON).content(jsonBody))
                .andExpect(status().isConflict());

        verify(lorryService).save(any(LorryEntity.class));
    }

    @Test
    void getLorry_whenLrFound_shouldReturn200AndBody() throws Exception {
        Long lr = 1111L;

        LorryEntity entity = new LorryEntity();
        entity.setLr(lr);
        entity.setLorryNumber("MH12AB1234");

        when(lorryService.findByLr(lr)).thenReturn(Optional.of(entity));

        mockMvc.perform(
                get("/api/lorry/{lr}", lr).accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lr").value(lr))
                .andExpect(jsonPath("$.lorryNumber").value("MH12AB1234"));

        verify(lorryService).findByLr(lr);
    }

    @Test
    void getLorry_whenLrNotFound_shouldReturn404() throws Exception {
        Long lr = 1111L;

        when(lorryService.findByLr(lr)).thenReturn(Optional.empty());

        mockMvc.perform(
                get("/api/lorry/{lr}", lr).accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

        verify(lorryService).findByLr(lr);
    }

    @Test
    void update_whenValid_shouldReturn200AndBody() throws Exception {
        Long lr = 1111L;

        LorryRequestDto requestDto = new LorryRequestDto();
        requestDto.setLr(lr);
        requestDto.setLorryNumber("MH12AB1234");
        requestDto.setDate(LocalDate.now());
        requestDto.setWeight(BigDecimal.valueOf(100));
        requestDto.setConsignorName("Rashmi");
        requestDto.setToLocation("Pune");
        requestDto.setFromLocation("Nagpur");

        LorryEntity updatedEntity = new LorryEntity();
        updatedEntity.setLr(1111L);
        updatedEntity.setLorryNumber("MH12AB1234");
        updatedEntity.setDate(LocalDate.now());
        updatedEntity.setWeight(BigDecimal.valueOf(100));
        updatedEntity.setConsignorName("Rashmi");
        updatedEntity.setToLocation("Pune");
        updatedEntity.setFromLocation("Nagpur");

        when(lorryService.update(any(LorryEntity.class))).thenReturn(updatedEntity);

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                put("/api/lorry/{lr}", lr)
                        .contentType(MediaType.APPLICATION_JSON).content(jsonBody)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lr").value(lr))
                .andExpect(jsonPath("$.lorryNumber").value("MH12AB1234"));

        verify(lorryService).update(any(LorryEntity.class));
    }

    @Test
    void updateLorry_whenNotFound_shouldReturn404() throws Exception {
        Long lr = 1111L;

        LorryRequestDto requestDto = new LorryRequestDto();
        requestDto.setLr(lr);
        requestDto.setLorryNumber("MH12AB1234");
        requestDto.setDate(LocalDate.now());
        requestDto.setWeight(BigDecimal.valueOf(100));
        requestDto.setConsignorName("Rashmi");
        requestDto.setToLocation("Pune");
        requestDto.setFromLocation("Nagpur");

        when(lorryService.update(any(LorryEntity.class)))
                .thenThrow(new NoSuchElementException("LR not found: " + lr));

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        put("/api/lorry/{lr}", lr)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                )
                .andExpect(status().isNotFound());

        verify(lorryService).update(any(LorryEntity.class));
    }

    @Test
    void updateLorry_whenPathAndBodyLrMismatch_shouldReturn400() throws Exception {
        Long pathLr = 1111L;

        LorryRequestDto requestDto = new LorryRequestDto();
        requestDto.setLr(2222L);
        requestDto.setLorryNumber("MH12AB1234");
        requestDto.setDate(LocalDate.now());
        requestDto.setWeight(BigDecimal.valueOf(100));
        requestDto.setConsignorName("Rashmi");
        requestDto.setToLocation("Pune");
        requestDto.setFromLocation("Nagpur");

        String jsonBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(
                        put("/api/lorry/{lr}", pathLr)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonBody)
                )
                .andExpect(status().isBadRequest());

        verify(lorryService, never()).update(any(LorryEntity.class));
    }


    @Test
    void deleteLorry_whenLrFound_shouldReturn204() throws Exception {
        Long lr = 1111L;

        mockMvc.perform(
                delete("/api/lorry/{lr}", lr)
        )
                .andExpect(status().isNoContent());

        verify(lorryService).deleteByLr(lr);
    }

    @Test
    void deleteLorry_whenLrNotFound_shouldReturn404() throws Exception {
        Long lr = 1111L;

        doThrow(new NoSuchElementException("Lr not found: " + lr))
                .when(lorryService).deleteByLr(lr);

        mockMvc.perform(
                delete("/api/lorry/{lr}", lr)
        )
                .andExpect(status().isNotFound());

        verify(lorryService).deleteByLr(lr);
    }

}
