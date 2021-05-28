package com.example.playbookProjApplicationBackend.Quiz;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/quiz")
@AllArgsConstructor
public class QuizController {

    private QuizService QS;

    @GetMapping(path = "/{quiz_id}")
    public String getQuiz( @PathVariable("quiz_id") Long quiz_id){return QS.getQuiz(quiz_id);}
    @GetMapping(path = "/all/{quiz_id}")
    public String getAllQuizQuestionsForQuiz( @PathVariable("quiz_id")Long quiz_id){
        return QS.getAllQuizQuestionsForQuiz(quiz_id);
    }
    @GetMapping(path = "/all/answered/{quiz_id}")
    public String getAllAnsweredQuestionsForQuiz(@PathVariable("quiz_id")Long quiz_id){
        return QS.getAllAnsweredQuestionsForQuiz(quiz_id);
    }
    @GetMapping(path = "/all/answered/{quiz_id}/{player_id}")
    public String getAllAnsweredQuestionsForQuizByPlayer(@PathVariable("quiz_id")Long quiz_id ,@PathVariable("player_id") String player_id){
        return QS.getAllAnsweredQuestionsForQuizByPlayer(quiz_id,player_id);
    }
    @GetMapping(path = "/all/player-answers/{quiz_id}")
    public String getAllPlayerAnswersForQuiz(@PathVariable("quiz_id")Long quiz_id){return QS.getAllPlayerAnswersForQuiz(quiz_id);}
    @GetMapping(path = "/all/player-answers/{quiz_id}/{question_id}")
    public String getAllPlayerAnswersForQuizQuestion(@PathVariable("quiz_id")Long quiz_id ,@PathVariable("question_id") Long question_id){return QS.getAllPlayerAnswersForQuizQuestion(quiz_id, question_id);}
    @GetMapping(path = "/all/player-answers/{quiz_id}/player/{player_id}")
    public String getAllPlayerAnswersForQuizForPlayer(@PathVariable("quiz_id")Long quiz_id ,@PathVariable("player_id") String player_id){return QS.getAllPlayerAnswersForQuizForPlayer(quiz_id, player_id);}
    @GetMapping(path = "/attempt/{quiz_id}/player/{player_id}")
    public String countQuizPlayerAttempts(@PathVariable("quiz_id")Long quiz_id ,@PathVariable("player_id") String player_id){return QS.countQuizPlayerAttempts(quiz_id, player_id);}
    @PostMapping(path = "/quiz-question/new")
    public String addNewQuizQuestion(@RequestBody Map<String,Object> newQuestion){
        return QS.addNewQuizQuestion(newQuestion);
    }
    @PutMapping(path = "/update/{quiz_id}")
    public String updateQuiz(@PathVariable("quiz_id") Long quiz_id,@RequestBody Map<String,Object> quizUpdate){
        return QS.updateQuiz(quiz_id, quizUpdate);
    }

    //update quiz
    //deactivate all quizquestions for a quiz
    //delete all quiz questions for a quiz
}
