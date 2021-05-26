package com.example.playbookProjApplicationBackend.Quiz;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping(path = "api/v1/quiz-question")
@AllArgsConstructor
public class QuizQuestionController {

    private QuizQuestionService QQS;

    @GetMapping(path = "/all")
    public String getAllQuizQuestionsInDatabase(){
        return QQS.getAllQuizQuestionsInDatabase();
    }

    @GetMapping(path = "/all/{team_id}/{quiz_id}")
    public String getAllQuizQuestionsForQuiz(@PathVariable("team_id")Long id, @PathVariable("quiz_id")Long quiz_id){
        return QQS.getAllQuizQuestionsForQuiz(id,quiz_id);
    }
    @GetMapping(path = "/all/{team_id}/{quiz_id}/rand")
    public String getAllQuestionsForQuizRandom(@PathVariable("team_id")Long id, @PathVariable("quiz_id")Long quiz_id){
        return QQS.getAllQuestionsForQuizRandom(id,quiz_id);
    }

    @GetMapping(path = "/all/answered/{team_id}/{quiz_id}")
    public String getAllAnsweredQuestionsForQuiz(@PathVariable("team_id")Long id,@PathVariable("quiz_id")Long quiz_id){
        return QQS.getAllAnsweredQuestionsForQuiz(id,quiz_id);
    }
    @GetMapping(path = "/all/answered/count/{team_id}/{quiz_id}")
    public String getCountAllAnsweredQuestionsForQuiz(@PathVariable("team_id")Long id, @PathVariable("quiz_id")Long quiz_id){
        return QQS.getCountAllAnsweredQuestionsForQuiz(id,quiz_id);
    }
    @GetMapping(path = "/all/answered/{team_id}/{quiz_id}/{player_id}")
    public String getAllAnsweredQuestionsForQuizByPlayer(@PathVariable("team_id")Long id,@PathVariable("quiz_id")Long quiz_id ,@PathVariable("player_id") String player_id){
        return QQS.getAllAnsweredQuestionsForQuizByPlayer(id,quiz_id,player_id);
    }
    @PostMapping(path = "/new")
    public String insertNewQuizQuestion(@RequestBody Map<String,Object> newQuestion){
        return QQS.insertNewQuizQuestion(newQuestion);
    }

    @PutMapping(path = "/update/{team_id}/{question_id}")
    public String updateQuizQuestion(@PathVariable("team_id") Long team_id,@PathVariable("question_id") Long question_id,@RequestBody Map<String,Object> updates){
        return QQS.updateQuizQuestion(team_id,question_id,updates);
    }
    @DeleteMapping(path = "/delete/{question_id}")
    public String deleteAQuizQuestion(@PathVariable("question_id") Long question_id){
        return QQS.deleteAQuizQuestion(question_id);
    }
    @PutMapping(path = "/deactivate/{team_id}/{question_id}")
    public String deactivateQuizQuestion(@PathVariable("team_id")Long id, @PathVariable("question_id") Long question_id){
        return QQS.deactivateQuizQuestion(id,question_id);
    }

}
