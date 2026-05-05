package com.inventario.aceites;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AceitesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AceitesApplication.class, args);
	}
}
/*
@Bean
CommandLineRunner init(ProductoRepository repo) {
    return args -> {
        if (repo.count() == 0) {

            Producto p1 = new Producto();
            p1.setCodigo("A1");
            p1.setDescripcion("Aceite 10W40");
            p1.setGrado("10W40");
            repo.save(p1);

            Producto p2 = new Producto();
            p2.setCodigo("A2");
            p2.setDescripcion("Aceite 20W50");
            p2.setGrado("20W50");
            repo.save(p2);
        }
    };
}
*/
