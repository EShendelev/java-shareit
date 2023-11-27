package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoTest {
    @Autowired
    JacksonTester<ItemResponseDto> json;

    @Test
    void itemDtoTest() throws Exception {
        ItemResponseDto itemResponseDto = new ItemResponseDto(
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
        JsonContent<ItemResponseDto> result = json.write(itemResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("testName");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("testDescription");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
    }
}
