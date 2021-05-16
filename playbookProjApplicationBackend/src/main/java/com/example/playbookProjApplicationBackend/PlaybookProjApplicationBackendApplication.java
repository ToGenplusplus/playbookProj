package com.example.playbookProjApplicationBackend;

import com.example.playbookProjApplicationBackend.Team.Team;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PlaybookProjApplicationBackendApplication {

	//private static final Logger log = LoggerFactory.getLogger(PlaybookProjApplicationBackendApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(PlaybookProjApplicationBackendApplication.class, args);
	}

}
