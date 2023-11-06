package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.request.service.interfaces.RequestItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestItemController.class)
@AutoConfigureMockMvc
public class RequestItemControllerMock {
    @MockBean
    RequestItemService requestItemService;
    
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    ObjectMapper mapper;

    private static final String SHAREIT_HEADER = "X-Sharer-User-Id";
    @Test
    void findAllTest() throws Exception {
        RequestItemDto request = new RequestItemDto(
                1L,
                "testDescription",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        List<RequestItemDto> requests = List.of(request);

        Mockito.when(requestItemService.findAll(
                        Mockito.any(Long.class), Mockito.any(Integer.class), Mockito.any(Integer.class)))
                .thenReturn(requests);

        mockMvc.perform(get("/requests/all")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(request.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description",
                        Matchers.is(request.getDescription()),
                        String.class));

        Mockito.verify(requestItemService, Mockito.times(1))
                .findAll(Mockito.any(Long.class), Mockito.any(Integer.class), Mockito.any(Integer.class));
    }

    @Test
    void findAllByUserIdTest() throws Exception {
        RequestItemDto request = new RequestItemDto(
                1L,
                "testDescription",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        List<RequestItemDto> requests = List.of(request);

        Mockito.when(requestItemService.findAllByUserId(Mockito.any(Long.class)))
                .thenReturn(requests);

        mockMvc.perform(get("/requests")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.[0].id", Matchers.is(request.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description",
                        Matchers.is(request.getDescription()),
                        String.class));

        Mockito.verify(requestItemService, Mockito.times(1))
                .findAllByUserId(Mockito.any(Long.class));
    }

    @Test
    void findByIdTest() throws Exception {
        RequestItemDto requestInfoDto = new RequestItemDto(
                1L,
                "testDescription",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        Mockito.when(requestItemService.findById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(requestInfoDto);

        mockMvc.perform(get("/requests/1")
                        .header(SHAREIT_HEADER, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",
                        Matchers.is(requestInfoDto.getDescription()),
                        String.class));

        Mockito.verify(requestItemService, Mockito.times(1))
                .findById(Mockito.anyLong(), Mockito.anyLong());
    }

    @Test
    void saveTest() throws Exception {
        RequestItemDto itemRequestDto = new RequestItemDto(
                null,
                "testDescription",
                null,
                null);
        RequestItemDto requestInfoDto = new RequestItemDto(1L,
                "testDescription",
                LocalDateTime.now(),
                new ArrayList<>()
        );

        Mockito.when(requestItemService.save(Mockito.any(Long.class), Mockito.any(RequestItemDto.class)))
                .thenReturn(requestInfoDto);

        mockMvc.perform(post("/requests")
                        .header(SHAREIT_HEADER, 1L)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(requestInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.description",
                        Matchers.is(requestInfoDto.getDescription()),
                        String.class));

        Mockito.verify(requestItemService, Mockito.times(1))
                .save(Mockito.any(Long.class), Mockito.any(RequestItemDto.class));
    }
}
