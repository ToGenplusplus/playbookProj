package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer,Long> {

 @Query(value="SELECT AVG(pa.answered_time) FROM player_answers pa WHERE pa.player_id=:" +
            "player_id AND pa.player_id IN(SELECT p.player_id FROM players p WHERE p.team_id = :team_id)",nativeQuery = true)
    float getPlayerAverageAnswerSpeed(@Param("team_id") Long team_id,@Param("player_id")String player);
}
