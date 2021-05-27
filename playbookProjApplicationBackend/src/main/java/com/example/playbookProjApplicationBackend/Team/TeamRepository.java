package com.example.playbookProjApplicationBackend.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeamRepository extends JpaRepository<Team,Long> {

    @Modifying
    @Query(value = "DELETE FROM player_answers pa USING quiz_questions qq WHERE qq.id = pa.question_id AND qq.team_id IN (SELECT t.id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id);" +
            "DELETE FROM quiz_questions qq WHERE qq.team_id IN (SELECT id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    void deleteAllTeamQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id);
    @Modifying
    @Query(value = "DELETE FROM player_answers pa USING quiz_questions qq WHERE qq.id = pa.question_id AND qq.question_type = :type AND qq.team_id IN (SELECT t.id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id);" +
            "DELETE FROM quiz_questions qq WHERE qq.question_type = :type AND qq.team_id IN (SELECT id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    void deleteAllTeamPositionQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id,@Param("type")String type);
    @Modifying
    @Query(value = "UPDATE quiz_questions SET is_active= NOT is_active WHERE team_id IN (SELECT t.id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    void deactivateAllTeamQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id);
    @Modifying
    @Query(value = "UPDATE quiz_questions SET is_active= NOT is_active WHERE question_type = :type AND team_id IN (SELECT t.id FROM team t WHERE t.id = :team_id AND t.organization_id = :org_id)",nativeQuery = true)
    void deactivateAllTeamPositionQuestions(@Param("org_id")Long org_id,@Param("team_id")Long team_id,@Param("type")String type);
}
