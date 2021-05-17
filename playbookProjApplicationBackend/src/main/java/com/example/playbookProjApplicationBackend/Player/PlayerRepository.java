package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    @Query(value="SELECT * FROM players pl WHERE pl.team_id = :team_id", nativeQuery = true)
    List<Player> getPlayersByTeamId(@Param("team_id")Long id);

}
