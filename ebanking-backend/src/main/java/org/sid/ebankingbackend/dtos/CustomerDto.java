package org.sid.ebankingbackend.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String email;
}
