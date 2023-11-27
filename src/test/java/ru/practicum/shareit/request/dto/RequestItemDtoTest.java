package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestItemDtoTest {
    @Autowired
    JacksonTester<RequestItemDto> json;

    @Test
    void itemRequestDtoTest() throws Exception {
        RequestItemDto requestItemDto = new RequestItemDto(
                1L,
                "testDescription",
                LocalDateTime.of(2023, 12, 24, 12, 30),
                null);

        JsonContent<RequestItemDto> result = json.write(requestItemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("testDescription");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2023-12-24T12:30:00");
    }
}
