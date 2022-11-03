package com.practica.hitoindividualaccesodatos.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Account {
    private String id;
    private String nombre;
    private String dni;
    private String cc;
    private Double balance;
    private LocalDate fechaCreacion;
}
