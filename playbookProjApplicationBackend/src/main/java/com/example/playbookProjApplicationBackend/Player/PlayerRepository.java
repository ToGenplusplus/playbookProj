package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, String> {

    @Query(value="SELECT * FROM players p where p.team_id = :team_id AND p.student_number IN " +
            "(SELECT player_id FROM player_positions WHERE position_id = :position_id)", nativeQuery = true)
    List<Player> getPlayersByTeamPosition(@Param("team_id")Long team_id, @Param("position_id") String position_id);
    @Query(value="SELECT * FROM players p WHERE p.team_id = :team_id AND p.email = :email", nativeQuery = true)
    Player getPlayerByEmail(@Param("team_id") Long team_id,@Param("email")String email);
    @Modifying
    @Query(value = "INSERT INTO player_positions(player_id,position_id)VALUES(:player_id,:pos)",nativeQuery = true)
    void insertNewPlayerPosition(@Param("player_id") String id, @Param("pos") String pos);
    @Modifying
    @Query(value = "DELETE FROM player_positions WHERE player_id = :player_id",nativeQuery = true)
    void removePlayerPosition(@Param("player_id")String player_id);
}
