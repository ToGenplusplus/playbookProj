package com.example.playbookProjApplicationBackend.Quiz;

import lombok.AllArgsConstructor;
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

    @PostMapping(path = "/new")
    public String insertNewQuizQuestion(@RequestBody Map<String,Object> newQuestion){
        return QQS.insertNewQuizQuestion(newQuestion);
    }
    @PutMapping(path = "/update/{quiz_id}/{question_id}")
    public String updateQuizQuestion(@PathVariable("quiz_id")Long quiz_id, @PathVariable("question_id") Long question_id,@RequestBody Map<String,Object> updates){
        return QQS.updateQuizQuestion(quiz_id, question_id,updates);
    }
    @PutMapping(path = "/deactivate/{quiz_id}/{question_id}")
    public String deactivateQuizQuestion(@PathVariable("quiz_id")Long quiz_id, @PathVariable("question_id") Long question_id){
        return QQS.deactivateQuizQuestion(quiz_id,question_id);
    }
    @DeleteMapping(path = "/delete/{quiz_id}/{question_id}")
    public String deleteAQuizQuestion(@PathVariable("quiz_id")Long quiz_id, @PathVariable("question_id") Long question_id){
        return QQS.deleteAQuizQuestion(quiz_id,question_id);
    }

}
