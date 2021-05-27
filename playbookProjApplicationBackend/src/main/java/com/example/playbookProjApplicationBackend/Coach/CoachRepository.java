package com.example.playbookProjApplicationBackend.Coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CoachRepository extends JpaRepository <Coach, Long>{
    @Query(value = "SELECT * FROM coach c WHERE c.team_id = :team_id AND c.email = :email",nativeQuery = true)
    Coach getCoachByEmail(@Param("team_id")Long id,@Param("email")String email);
    @Query(value="SELECT * FROM coach c where c.team_id = :team_id AND c.id IN " +
            "(SELECT coach_id FROM coach_positions WHERE position_id = :position_id)", nativeQuery = true)
    List<Coach> getCoachesByCoachPosition(@Param("team_id")Long team_id, @Param("position_id") String position_id);
    @Modifying
    @Query(value = "INSERT INTO coach_positions(coach_id,position_id)VALUES(:coach_id,:pos)",nativeQuery = true)
    void insertNewCoachPosition(@Param("coach_id") Long id, @Param("pos") String pos);
    @Modifying
    @Query(value = "DELETE FROM coach_positions WHERE coach_id = :coach_id",nativeQuery = true)
    void removeCoachPosition(@Param("coach_id")Long coach_id);
}
