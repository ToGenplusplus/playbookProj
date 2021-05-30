package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PlayerRepository extends JpaRepository<Player, String> {

    @Modifying
    @Query(value = "DELETE FROM player_positions pp WHERE (pp.player_id = :player_id);" +
            "DELETE FROM player_quizzes_taken pqt WHERE (pqt.player_id = :player_id);" +
            "DELETE FROM player_answers pa WHERE (pa.player_id = :player_id);" +
            "DELETE FROM players p WHERE (p.player_id = :player_id);",nativeQuery = true)
    void deletePlayer(@Param("player_id")String player_id);
}
