package com.bailaconsarabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * La clase principal de la aplicación Bailaconsara.
 * 
 * Esta clase se encarga de iniciar la aplicación Spring Boot y habilitar la 
 * ejecución asíncrona. La anotación {@link SpringBootApplication} indica que 
 * es una clase de configuración que también habilita la configuración 
 * automática y la búsqueda de componentes.
 * 
 * La anotación {@link EnableAsync} permite que los métodos anotados con 
 * {@link org.springframework.scheduling.annotation.Async} se ejecuten de 
 * forma asíncrona, lo que permite mejorar la eficiencia y el rendimiento 
 * de la aplicación al realizar tareas en segundo plano.
 */
@SpringBootApplication
@EnableAsync
public class BailaconsaraBackendApplication {

    /**
     * Método principal que se utiliza para ejecutar la aplicación.
     * 
     * @param args los argumentos de línea de comandos pasados al programa.
     */
	public static void main(String[] args) {
		SpringApplication.run(BailaconsaraBackendApplication.class, args);
	}

}
