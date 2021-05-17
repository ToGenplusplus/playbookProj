package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    @Query(value="SELECT * FROM players WHERE team_id = ?1")
    List<Player> getPlayersByTeamId(Long id);

    @Query(value = "SELECT * FROM players WHERE team_id = ?1 and ")
    List<Player> getPlayersByPosition(Long team_id, String position);
}
