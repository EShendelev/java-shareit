package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.interfaces.CommentService;
import ru.practicum.shareit.item.service.interfaces.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerMockTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private CommentService commentService;

    @Autowired
    private MockMvc mvc;

    private ItemResponseDto itemResponseDto;

    private CommentDto commentDto;

    @BeforeEach
    void init() {
        itemResponseDto = new ItemResponseDto(
                1L,
                "testName",
                "testDescription",
                true,
                null,
                null,
                null,
                null,
                null
        );

        commentDto = new CommentDto(1L, "text", null, null);
    }

    @Test
    void findByOwnerIdTest() throws Exception {
        when(itemService.findAllByOwnerId(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemResponseDto));
        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(itemResponseDto)))
                );
    }

    @Test
    void findByIdTest() throws Exception {
        when(itemService.findById(anyLong(), anyLong())).thenReturn(itemResponseDto);

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemResponseDto)));
    }

    @Test
    void saveTest() throws Exception {
        when(itemService.save(any(ItemRequestDto.class), anyLong())).thenReturn(itemResponseDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemResponseDto)));
    }

    @Test
    void saveCommentTest() throws Exception {
        when(commentService.save(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(commentDto)));
    }

    @Test
    void updateTest() throws Exception {
        when(itemService.update(anyLong(), anyLong(), any(ItemRequestDto.class))).thenReturn(itemResponseDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(itemResponseDto)));
    }
}
