package com.example.playbookProjApplicationBackend.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team,Long> {

    @Query(value = "SELECT * FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id",nativeQuery = true)
    Team findTeamById(@Param("org_id")Long org_id,@Param("team_id")Long team_id);
    @Query(value = "SELECT * FROM team t WHERE t.name = :team_name AND t.organization_id = :org_id",nativeQuery = true)
    Team findTeamByName(@Param("org_id")Long org_id,@Param("team_name")String teamName);
    @Query(value = "SELECT * FROM team t WHERE t.organization_id = :org_id",nativeQuery = true)
    List<Team> getTeamsInOrganization(@Param("org_id")Long org_id);
}
