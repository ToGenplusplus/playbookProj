package com.example.playbookProjApplicationBackend.Coach;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CoachRepository extends JpaRepository <Coach, Long>{
    @Modifying
    @Query(value = "DELETE FROM player_quizzes_taken WHERE quiz_id IN (SELECT quiz_id FROM quiz WHERE coach_id = :coach_id);" +
            "DELETE FROM coach_positions WHERE (coach_id = :coach_id);" +
            "DELETE FROM player_answers pa USING quiz_questions qq WHERE qq.id = pa.question_id AND qq.quiz_id IN (SELECT q.quiz_id FROM quiz q WHERE q.coach_id= :coach_id);" +
            "DELETE FROM quiz_questions WHERE quiz_id IN (SELECT quiz_id FROM quiz WHERE coach_id = :coach_id);" +
            "DELETE FROM quiz WHERE (coach_id = :coach_id);" +
            "DELETE FROM coach WHERE (id = :coach_id);",nativeQuery = true)
    void removeCoach(@Param("coach_id")Long coach_id);
}
