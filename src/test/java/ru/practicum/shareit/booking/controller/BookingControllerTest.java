package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingControllerTest {


    @Autowired
    private BookingController bookingController;
    @Autowired
    private UserController userController;
    @Autowired
    private ItemController itemController;
    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private UserDto userDto1;
    private BookingRequestDto bookingRequestDto;

    @BeforeEach
    void init() {
        itemRequestDto = new ItemRequestDto(
                null,
                "testName",
                "testDescription",
                true,
                null
        );

        userDto = new UserDto(null, "testName", "test@mail.com");
        userDto1 = new UserDto(null, "testName1", "test1@mail.com");

        bookingRequestDto = new BookingRequestDto(
                null,
                LocalDateTime.of(2023, 10, 24, 12, 30),
                LocalDateTime.of(2023, 11, 10, 13, 0),
                null);
    }

    @Test
    void saveTest() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingResponseDto booking = bookingController.save(user1.getId(), bookingRequestDto);
        assertEquals(1L, bookingController.findById(user1.getId(), booking.getId()).getId());
    }

    @Test
    void saveTestWithWrongUser() {
        assertThrows(NotFoundException.class,
                () -> bookingController.save(1L, bookingRequestDto));
    }

    @Test
    void saveTestWithWrongItem() {
        bookingRequestDto.setItemId(30L);
        UserDto user1 = userController.save(userDto1);
        assertThrows(NotFoundException.class,
                () -> bookingController.save(user1.getId(), bookingRequestDto));
    }

    @Test
    void saveTestWithOwner() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        assertThrows(NotFoundException.class,
                () -> bookingController.save(user.getId(), bookingRequestDto));
    }

    @Test
    void saveTestWithUnavailableItem() {
        UserDto user = userController.save(userDto);
        itemRequestDto.setAvailable(false);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        assertThrows(ValidateException.class,
                () -> bookingController.save(user1.getId(), bookingRequestDto));
    }

    @Test
    void updateStateTest() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingResponseDto booking = bookingController.save(user1.getId(), bookingRequestDto);
        assertEquals(Status.WAITING, bookingController.findById(user1.getId(), booking.getId()).getStatus());
        bookingController.updateState(user.getId(), booking.getId(), true);
        assertEquals(Status.APPROVED, bookingController.findById(user1.getId(), booking.getId()).getStatus());
    }

    @Test
    void updateStateTestWithWrongBooking() {
        assertThrows(NotFoundException.class,
                () -> bookingController.updateState(1L, 1L, true));
    }

    @Test
    void updateStateTestWithWrongUser() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), bookingRequestDto);
        assertThrows(NotFoundException.class,
                () -> bookingController.updateState(1L, 2L, true));
    }

    @Test
    void findAllByUserTest() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingResponseDto booking = bookingController.save(user1.getId(), bookingRequestDto);
        assertEquals(1, bookingController
                .findAllByState(user1.getId(), "WAITING", 0, 10).size());
        assertEquals(1, bookingController
                .findAllByState(user1.getId(), "ALL", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByState(user1.getId(), "PAST", 0, 10).size());
        assertEquals(1, bookingController
                .findAllByState(user1.getId(), "CURRENT", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByState(user1.getId(), "FUTURE", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByState(user1.getId(), "REJECTED", 0, 10).size());
        bookingController.updateState(booking.getId(), user.getId(), true);
        assertEquals(0, bookingController
                .findAllByState(user.getId(), "CURRENT", 0, 10).size());
        assertEquals(1, bookingController
                .findAllByOwnerIdAndState(user.getId(), "ALL", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByOwnerIdAndState(user.getId(), "WAITING", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByOwnerIdAndState(user.getId(), "FUTURE", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByOwnerIdAndState(user.getId(), "REJECTED", 0, 10).size());
        assertEquals(0, bookingController
                .findAllByOwnerIdAndState(user.getId(), "PAST", 0, 10).size());
    }

    @Test
    void findAllTestsWithWrongUserId() {
        assertThrows(NotFoundException.class, () -> bookingController
                .findAllByState(1L, "ALL", 0, 10));
        assertThrows(NotFoundException.class, () -> bookingController
                .findAllByOwnerIdAndState(1L, "ALL", 0, 10));
    }

    @Test
    void findAllTestsWithWrongForm() {
        assertThrows(ValidateException.class, () -> bookingController
                .findAllByState(1L, "ALL", -1, 10));
        assertThrows(ValidateException.class, () -> bookingController
                .findAllByOwnerIdAndState(1L, "ALL", -1, 10));
    }

    @Test
    void findByIdTestWithWrongId() {
        assertThrows(NotFoundException.class,
                () -> bookingController.findById(1L, 1L));
    }

    @Test
    void findByWrongUserTest() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), bookingRequestDto);
        assertThrows(NotFoundException.class, () -> bookingController.findById(1L, 10L));
    }

    @Test
    void deleteTest() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        bookingRequestDto.setItemId(item.getId());
        UserDto user1 = userController.save(userDto1);
        BookingResponseDto booking = bookingController.save(user1.getId(), bookingRequestDto);
        assertEquals(1L, bookingController.findById(user.getId(), booking.getId()).getId());
        bookingController.delete(booking.getId());
        assertThrows(NotFoundException.class, () -> bookingController.findById(user.getId(), booking.getId()));
    }
}