package com.example.playbookProjApplicationBackend.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer,Long> {

    @Query(value="SELECT * FROM player_answers pa WHERE pa.question_id IN " +
            "(SELECT qq.id FROM quiz_questions qq WHERE qq.question_type = :type and qq.team_id= :team_id)",nativeQuery = true)
    List<PlayerAnswer> getAllPlayerAnswersInTeamByQuestionType(@Param("team_id") Long team_id,@Param("type") String type);
    @Query(value="SELECT * FROM player_answers pa WHERE pa.player_id IN " +
            "(SELECT p.student_number FROM players p LEFT JOIN player_positions pp on pp.player_id = p.student_number WHERE pp.position_id = :position_id AND p.team_id = :team_id)",nativeQuery = true)
    List<PlayerAnswer> getAllPlayerAnswersInTeamByPosition(@Param("team_id") Long team_id,@Param("position_id")String position);
    @Query(value="SELECT * FROM player_answers pa WHERE pa.player_id IN " +
            "(SELECT p.student_number FROM players p LEFT JOIN player_positions pp on pp.player_id = p.student_number WHERE " +
            "pp.position_id = :position_id AND pp.player_id = :player_id AND p.team_id = :team_id)",nativeQuery = true)
    List<PlayerAnswer> getAllPlayerAnswersInTeamByPositionByPlayer(@Param("team_id") Long team_id,@Param("position_id")String position,@Param("player_id")String player);
    @Query(value="SELECT AVG(pa.answered_time) FROM player_answers pa WHERE pa.question_id IN " +
            "(SELECT qq.id FROM quiz_questions qq WHERE qq.question_type = :type and qq.team_id = :team_id)",nativeQuery = true)
    Object getTotalAverageAnswerSpeedForQuizCategory(@Param("team_id") Long team_id,@Param("type") String category);
    @Query(value="SELECT AVG(pa.answered_time) FROM player_answers pa WHERE pa.player_id=:" +
            "player_id AND pa.player_id IN(SELECT p.student_number FROM players p WHERE p.team_id = :team_id)",nativeQuery = true)
    float getPlayerAverageAnswerSpeed(@Param("team_id") Long team_id,@Param("player_id")String player);
    @Query(value="SELECT COUNT(*) FROM player_answers pa WHERE pa.player_id=:player_id AND pa.question_id = :question_id AND pa.player_id IN " +
            "(SELECT p.student_number FROM players p WHERE p.team_id = :team_id)",nativeQuery = true)
    int getCountPlayerAnswerForQuestion(@Param("team_id") Long team_id,@Param("player_id")String player,@Param("question_id")Long question_id);
}
