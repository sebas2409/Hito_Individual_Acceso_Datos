package com.practica.hitoindividualaccesodatos.service.dto;

import lombok.Builder;

@Builder
public record DepositResponse(String id,String nombre,Double balance) {
}
