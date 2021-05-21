package com.example.playbookProjApplicationBackend.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    @Modifying
    @Query(value = "DELETE FROM quiz_questions qq WHERE qq.team_id IN (SELECT id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    int deleteAllTeamQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id);
    @Modifying
    @Query(value = "UPDATE quiz_questions SET is_active= NOT is_active WHERE team_id IN (SELECT t.id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    int deactivateAllTeamQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id);
    @Modifying
    @Query(value = "UPDATE quiz_questions SET is_active= NOT is_active WHERE question_type = :type AND team_id IN (SELECT t.id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    int deactivateAllTeamPositionQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id,@Param("type")String type);
}
