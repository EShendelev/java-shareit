package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.interfaces.BookingService;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerMockTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingResponseDto bookingResponseDto;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void init() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("testName");
        userDto.setEmail("test@mail.ru");

        ItemResponseDto itemDto = new ItemResponseDto();
        itemDto.setId(1L);
        itemDto.setName("testName");
        itemDto.setDescription("testDescription");
        itemDto.setAvailable(true);

        bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(1L);
        bookingResponseDto.setStart(LocalDateTime.of(2024, 12, 12, 10, 0));
        bookingResponseDto.setEnd(LocalDateTime.of(2024, 12, 20, 10, 0));
        bookingResponseDto.setBooker(new BookingResponseDto.Booker(userDto.getId(), userDto.getName()));
        bookingResponseDto.setItem(new BookingResponseDto.BookingItemDto(itemDto.getId(), itemDto.getName()));

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setId(1L);
        bookingRequestDto.setStart(LocalDateTime.of(2024, 12, 12, 10, 0));
        bookingRequestDto.setEnd(LocalDateTime.of(2024, 12, 20, 10, 0));
        bookingRequestDto.setItemId(1L);
    }

    @Test
    void saveTest() throws Exception {
        when(bookingService.save(1L, bookingRequestDto))
                .thenReturn(bookingResponseDto);
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(mapper.writeValueAsString(bookingResponseDto)));
    }

    @Test
    void saveTestBookingStartAfterEnd() throws Exception {
        bookingRequestDto.setStart(LocalDateTime.now().plusDays(5));
        bookingRequestDto.setEnd(LocalDateTime.now().plusDays(2));
        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateStatusTest() throws Exception {
        bookingResponseDto.setStatus(Status.APPROVED);
        when(bookingService.updateState(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResponseDto)));
    }

    @Test
    void findAllByOwnerIdAndStatusTest() throws Exception {
        when(bookingService.getAllByOwnerIdAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));
        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @Test
    void findAllByUserTest() throws Exception {
        when(bookingService.getAllByState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));
        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 2L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingResponseDto))));
    }

    @Test
    void findAllByUserTestWithWrongStatus() throws Exception {
        when(bookingService.getAllByOwnerIdAndState(anyLong(), anyString(), anyInt(), anyInt()))
                .thenThrow(ValidateException.class);
        mvc.perform(get("/bookings?state=text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void findByIdTest() throws Exception {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);
        mvc.perform(get("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingResponseDto)));
    }
}
