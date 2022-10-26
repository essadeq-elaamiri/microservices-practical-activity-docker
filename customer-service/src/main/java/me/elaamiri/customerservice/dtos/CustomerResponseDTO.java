package me.elaamiri.customerservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerResponseDTO {
    private String id;
    private String name;
    private String email;
}
