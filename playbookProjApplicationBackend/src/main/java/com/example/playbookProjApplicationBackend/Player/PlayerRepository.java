package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    @Query(value="SELECT * FROM players p WHERE p.team_id = :team_id", nativeQuery = true)
    List<Player> getPlayersByTeamId(@Param("team_id")Long id);

    @Query(value="SELECT * FROM players p where p.team_id = :team_id AND p.student_number IN " +
            "(SELECT player_id FROM player_positions WHERE position_id = :position_id)", nativeQuery = true)
    List<Player> getPlayersByTeamPosition(@Param("team_id")Long team_id, @Param("position_id") String position_id);
    @Query(value="SELECT * FROM players p WHERE p.team_id = :team_id AND p.email = :email", nativeQuery = true)
    Player getPlayerByEmail(@Param("team_id") Long team_id,@Param("email")String email);
}
