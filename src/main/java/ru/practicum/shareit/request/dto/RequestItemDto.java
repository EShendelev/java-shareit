package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.validmark.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RequestItemDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String description;
    private LocalDateTime created;
    private List<ItemResponseDto> items;
}
