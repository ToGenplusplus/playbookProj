package com.example.playbookProjApplicationBackend;

import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Player.PlayerRepository;
import com.example.playbookProjApplicationBackend.Player.PlayerService;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestionRepository;
import com.example.playbookProjApplicationBackend.Team.Team;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class PlaybookProjApplicationBackendApplication {

	private static final Logger log = LoggerFactory.getLogger(PlaybookProjApplicationBackendApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(PlaybookProjApplicationBackendApplication.class, args);
	}

/*
	@Bean
	public CommandLineRunner commandLineRunner(QuizQuestionRepository QR) {
		return (args) -> {
			String pos = "OL";
			Long id = 1l;
			String player_id = "748696152";
			log.info("Testing out quiz repository retrieval queiries\n");
			log.info("---------------------------\n");
		};
	}
*/


}
