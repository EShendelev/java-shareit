package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidateException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.controller.RequestItemController;
import ru.practicum.shareit.request.dto.RequestItemDto;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemControllerTest {
    @Autowired
    private ItemController itemController;
    @Autowired
    private UserController userController;
    @Autowired
    private BookingController bookingController;
    @Autowired
    private RequestItemController requestItemController;
    private ItemRequestDto itemRequestDto;
    private ItemRequestDto itemRequestDto1;
    private UserDto userDto;
    private UserDto userDto1;
    private RequestItemDto requestItemDto;
    private CommentDto commentDto;

    @BeforeEach
    void init() {
        itemRequestDto = new ItemRequestDto(
                null, "testName", "testDescription", true, null
        );
        itemRequestDto1 = new ItemRequestDto(
                null, "testName1", "testDescription1", true, null
        );

        userDto = new UserDto(null, "testName", "test@test.ru");
        userDto1 = new UserDto(null, "testName1", "test1@test.ru");

        requestItemDto = new RequestItemDto(null, "testDescription", null, null);

        commentDto = new CommentDto(null, "text", null, null);
    }

    @Test
    void searchByEmptyTextTest() {
        userController.save(userDto);
        itemController.save(itemRequestDto, 1L);
        assertEquals(new ArrayList<ItemResponseDto>(), itemController.searchByTextRequest("", 0, 10));
    }

    @Test
    void searchByTextTest() {
        userController.save(userDto);
        itemController.save(itemRequestDto, 1L);
        assertEquals(1, itemController.searchByTextRequest("desc", 0, 10).size());
    }

    @Test
    void searchByTextWithWrongFromTest() {
        assertThrows(IllegalArgumentException.class, () -> itemController.searchByTextRequest("a", -1, 10));
    }

    @Test
    void getAllUserItemsWithWrongFromTest() {
        userController.save(userDto);
        assertThrows(IllegalArgumentException.class,
                () -> itemController.getAllUserItems(1L, -1, 10));
    }

    @Test
    void saveTest() {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, 1L);
        assertEquals(item.getId(), itemController.findById(item.getId(), user.getId()).getId());
    }

    @Test
    void saveWithWrongUserTest() {
        assertThrows(NotFoundException.class, () -> itemController.save(itemRequestDto, 1L));
    }

    @Test
    void saveWithRequestTest() {
        UserDto user = userController.save(userDto);
        requestItemController.save(user.getId(), requestItemDto);
        itemRequestDto.setRequestId(1L);
        userController.save(userDto1);
        ItemResponseDto item = itemController.save(itemRequestDto, 2L);
        assertEquals(item.getId(), itemController.findById(1L, 2L).getId());
    }

    @Test
    void saveWithWrongItemRequestTest() {
        itemRequestDto.setRequestId(10L);
        userController.save(userDto);
        assertThrows(NotFoundException.class, () -> itemController.save(itemRequestDto, 1L));
    }

    @Test
    void saveCommentTest() throws InterruptedException {
        UserDto user = userController.save(userDto);
        ItemResponseDto item = itemController.save(itemRequestDto, user.getId());
        UserDto user1 = userController.save(userDto1);
        bookingController.save(user1.getId(), new BookingRequestDto(
                null, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item.getId()));
        bookingController.updateState(1L, user.getId(), true);
        TimeUnit.SECONDS.sleep(2);
        itemController.saveComment(user1.getId(), item.getId(), commentDto);
        assertEquals(1, itemController.findById(1L, 1L).getComments().size());
    }

    @Test
    void saveCommentWithWrongUserIdTest() {
        assertThrows(NotFoundException.class, () -> itemController.saveComment(1L, 1L, commentDto));
    }

    @Test
    void saveCommentWithWrongItemIdTest() {
        userController.save(userDto);
        assertThrows(NotFoundException.class, () -> itemController.saveComment(1L, 1L, commentDto));
        itemController.save(itemRequestDto, 1L);
        assertThrows(ValidateException.class, () -> itemController.saveComment(1L, 1L, commentDto));
    }

    @Test
    void updateTest() {
        userController.save(userDto);
        itemController.save(itemRequestDto, 1L);
        itemController.update(itemRequestDto1, 1L, 1L);
        assertEquals(itemRequestDto1.getDescription(), itemController.findById(1L, 1L).getDescription());
    }

    @Test
    void updateWithWrongItemIdTest() {
        assertThrows(NotFoundException.class, () -> itemController.update(itemRequestDto, 1L, 1L));
    }

    @Test
    void updateWithWrongUserIdTest() {
        userController.save(userDto);
        itemController.save(itemRequestDto, 1L);
        assertThrows(NotFoundException.class, () -> itemController.update(itemRequestDto1, 1L, 10L));
    }

    @Test
    void deleteTest() {
        userController.save(userDto);
        itemController.save(itemRequestDto, 1L);
        assertEquals(1, itemController.getAllUserItems(1L, 0, 10).size());
        itemController.delete(1L, 1L);
        assertEquals(0, itemController.getAllUserItems(1L, 0, 10).size());
    }
}
