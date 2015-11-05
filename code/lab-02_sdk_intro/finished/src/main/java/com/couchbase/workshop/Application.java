package com.couchbase.workshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The entry point into the application.
 *
 * Run the {@link #main(String...)} method to start the application on port 8080.
 */
@SpringBootApplication
@ComponentScan({ "com.couchbase.workshop" })
public class Application {

    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }

}