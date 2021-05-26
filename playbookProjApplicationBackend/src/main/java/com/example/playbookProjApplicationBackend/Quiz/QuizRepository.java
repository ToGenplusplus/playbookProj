package com.example.playbookProjApplicationBackend.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.quiz_id = :quiz_id", nativeQuery = true)
    List<QuizQuestion> getAllQuizQuestionsForQuiz(@Param("quiz_id")Long id);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.quiz_id = :quiz_id  ORDER BY random()", nativeQuery = true)
    List<QuizQuestion> getAllQuestionsForQuizRandom(@Param("quiz_id")Long id);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.quiz_id = :quiz_id AND qq.id IN (SELECT pa.question_id FROM player_answers pa)", nativeQuery = true)
    List<QuizQuestion> getAllAnsweredQuestionsForQuiz(@Param("quiz_id")Long id);

    @Query(value = "SELECT * FROM quiz_questions qq WHERE qq.quiz_id = :quiz_id AND qq.id IN (SELECT pa.question_id FROM player_answers pa WHERE pa.player_id = :player_id)", nativeQuery = true)
    List<QuizQuestion> getAllAnsweredQuestionsForQuizByPlayer(@Param("quiz_id")Long id, @Param("player_id")String player_id);
}
