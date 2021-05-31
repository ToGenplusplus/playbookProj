package com.example.playbookProjApplicationBackend.Team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TeamRepository extends JpaRepository<Team,Long> {

    @Modifying
    @Query(value = "DELETE FROM player_answers pa USING quiz_questions qq WHERE qq.id = pa.question_id AND qq.quiz_id IN (SELECT q.quiz_id FROM quiz q WHERE q.team_id = :team_id);" +
            "DELETE FROM quiz_questions qq WHERE qq.quiz_id IN (SELECT q.quiz_id FROM quiz q WHERE q.team_id = :team_id);" +
            "DELETE FROM player_quizzes_taken pqt WHERE pqt.quiz_id IN (SELECT q.quiz_id FROM quiz q WHERE q.team_id = :team_id);" +
            "DELETE FROM quiz q WHERE (q.team_id = :team_id);",nativeQuery = true)
    void deleteAllQuizzesForTeam(@Param("team_id")Long team_id);
}
