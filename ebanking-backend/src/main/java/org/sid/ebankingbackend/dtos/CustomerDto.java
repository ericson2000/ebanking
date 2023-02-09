package org.sid.ebankingbackend.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CustomerDto {

    private Long id;

    private String name;

    private String email;
}
