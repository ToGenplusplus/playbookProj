package com.example.playbookProjApplicationBackend.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {

    @Modifying
    @Query(value = "UPDATE quiz_questions SET is_active = NOT is_active WHERE id = :question_id AND quiz_id = :quiz_id", nativeQuery = true)
    void deactivateQuizQuestion(@Param("quiz_id") Long quiz_id,@Param("question_id") Long question_id);
}
