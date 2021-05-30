package com.example.playbookProjApplicationBackend.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface QuizRepository extends JpaRepository<Quiz,Long> {
    @Modifying
    @Query(value = "DELETE FROM player_answers pa WHERE pa.question_id IN (SELECT qq.id FROM quiz_questions qq WHERE qq.quiz_id = :quiz_id);" +
            "DELETE FROM quiz_questions qq WHERE (qq.quiz_id = :quiz_id);" +
            "DELETE FROM player_quizzes_taken pqt WHERE (pqt.quiz_id = :quiz_id);" +
            "DELETE FROM quiz q WHERE (q.quiz_id = :quiz_id);",nativeQuery = true)
    void deleteQuiz(@Param("quiz_id") Long quiz_id);
}
