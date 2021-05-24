package com.example.playbookProjApplicationBackend.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.team_id = :team_id", nativeQuery = true)
    List<QuizQuestion> getAllQuestionsForTeam(@Param("team_id")Long id);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.team_id = :team_id ORDER BY random()", nativeQuery = true)
    List<QuizQuestion> getAllQuestionsForTeamRandom(@Param("team_id")Long id);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.team_id = :team_id AND qq.question_type = :position", nativeQuery = true)
    List<QuizQuestion> getAllQuestionsForTeamByPosition(@Param("team_id")Long id, @Param("position")String position);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.team_id = :team_id AND qq.question_type = :position ORDER BY random()", nativeQuery = true)
    List<QuizQuestion> getAllQuestionsForTeamByPositionRandom(@Param("team_id")Long id, @Param("position")String position);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.team_id = :team_id AND qq.id IN (SELECT pa.question_id FROM player_answers pa)", nativeQuery = true)
    List<QuizQuestion> getAllAnsweredQuestionsForTeam(@Param("team_id")Long id);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.team_id = :team_id AND qq.id IN (SELECT pa.question_id FROM player_answers pa WHERE pa.player_id = :player_id)", nativeQuery = true)
    List<QuizQuestion> getAllAnsweredQuestionsForTeamByPlayer(@Param("team_id")Long id, @Param("player_id")String player_id);

    @Query(value = "SELECT COUNT(*) FROM quiz_questions qq WHERE qq.team_id = :team_id AND qq.id IN (SELECT pa.question_id FROM player_answers pa) AND qq.question_type = :type", nativeQuery = true)
    Integer getCountAnsweredQuestionsForTeamByCategory(@Param("team_id")Long id, @Param("type")String position);

    @Modifying
    @Query(value = "UPDATE quiz_questions SET is_active = NOT is_active WHERE id = :question_id AND team_id = :team_id", nativeQuery = true)
    int deactivateQuizQuestion(@Param("team_id") Long team_id,@Param("question_id") Long question_id);
}
