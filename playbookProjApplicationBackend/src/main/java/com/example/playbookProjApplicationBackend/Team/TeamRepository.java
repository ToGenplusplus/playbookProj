package com.example.playbookProjApplicationBackend.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query(value = "SELECT * FROM team t WHERE t.name = :team_name",nativeQuery = true)
    Team findTeamByName(@Param("team_name")String teamName);
}
