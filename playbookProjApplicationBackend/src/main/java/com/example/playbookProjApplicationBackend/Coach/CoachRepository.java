package com.example.playbookProjApplicationBackend.Coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoachRepository extends JpaRepository <Coach, Long>{
    @Query(value="SELECT * FROM coach c WHERE c.team_id = :team_id", nativeQuery = true)
    List<Coach> getCoachesByTeamId(@Param("team_id")Long id);

    @Query(value="SELECT * FROM coach c where c.team_id = :team_id AND c.id IN " +
            "(SELECT coach_id FROM coach_positions WHERE position_id = :position_id)", nativeQuery = true)
    List<Coach> getCoachesByCoachPosition(@Param("team_id")Long team_id, @Param("position_id") String position_id);
}
