package com.odos.odos_server;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Stream;

@SpringBootApplication
public class Application {


  public static void main(String[] args) {
    // Application.java
    Dotenv dotenv1 = Dotenv.configure()
            .directory("env")
            .filename("db.env")
            .load();

    Dotenv dotenv2 = Dotenv.configure()
            .directory("env")
            .filename("server.env")
            .load();

    // 두 개 병합해서 환경변수로 등록
    Stream.concat(dotenv1.entries().stream(), dotenv2.entries().stream())
            .forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

    SpringApplication.run(Application.class, args);


  }
}

// TODO: spotless + pre-commit
// TODO: docker setting
// TODO: discord bot
// TODO: .env setting
// TODO: application.yaml separate
