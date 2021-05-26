package com.example.playbookProjApplicationBackend.Quiz;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/quiz")
@AllArgsConstructor
public class QuizController {

    private QuizService QS;

    @GetMapping(path = "/all/{quiz_id}")
    public String getAllQuizQuestionsForQuiz( @PathVariable("quiz_id")Long quiz_id){
        return QS.getAllQuizQuestionsForQuiz(quiz_id);
    }
    @GetMapping(path = "/all/{quiz_id}/rand")
    public String getAllQuestionsForQuizRandom( @PathVariable("quiz_id")Long quiz_id){
        return QS.getAllQuestionsForQuizRandom(quiz_id);
    }

    @GetMapping(path = "/all/answered/{quiz_id}")
    public String getAllAnsweredQuestionsForQuiz(@PathVariable("quiz_id")Long quiz_id){
        return QS.getAllAnsweredQuestionsForQuiz(quiz_id);
    }
    @GetMapping(path = "/all/answered/count/{quiz_id}")
    public String getCountAllAnsweredQuestionsForQuiz( @PathVariable("quiz_id")Long quiz_id){
        return QS.getCountAllAnsweredQuestionsForQuiz(quiz_id);
    }
    @GetMapping(path = "/all/answered/{quiz_id}/{player_id}")
    public String getAllAnsweredQuestionsForQuizByPlayer(@PathVariable("quiz_id")Long quiz_id ,@PathVariable("player_id") String player_id){
        return QS.getAllAnsweredQuestionsForQuizByPlayer(quiz_id,player_id);
    }
}
