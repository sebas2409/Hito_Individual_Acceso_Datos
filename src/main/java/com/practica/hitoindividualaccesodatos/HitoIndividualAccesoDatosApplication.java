package com.practica.hitoindividualaccesodatos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class HitoIndividualAccesoDatosApplication {

    public static void main(String[] args) {
        SpringApplication.run(HitoIndividualAccesoDatosApplication.class, args);
    }

}
