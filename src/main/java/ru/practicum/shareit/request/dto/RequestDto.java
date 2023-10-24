package ru.practicum.shareit.request.dto;

import java.time.LocalDateTime;

import ru.practicum.shareit.validmark.Create;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RequestDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String description;
    private LocalDateTime created;
    private List<ItemResponseDto> items;
}
