package com.lwen.gitsync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class GitSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitSyncApplication.class, args);
    }

}
