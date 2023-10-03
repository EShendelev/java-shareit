package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.validmark.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    @Email(groups = {Create.class})
    private String email;
}
