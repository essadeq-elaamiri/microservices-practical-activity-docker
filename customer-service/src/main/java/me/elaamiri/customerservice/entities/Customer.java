package me.elaamiri.customerservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor @Builder
@AllArgsConstructor
public class Customer {
    @Id
    private String id;
    @NotBlank
    private String name;
    @Email
    private String email;


}
