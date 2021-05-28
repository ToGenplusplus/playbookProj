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

    @PutMapping(path = "/update/{question_id}")
    public String updateQuizQuestion(@PathVariable("question_id") Long question_id,@RequestBody Map<String,Object> updates){
        return QQS.updateQuizQuestion(question_id,updates);
    }

    @DeleteMapping(path = "/delete/{question_id}")
    public String deleteAQuizQuestion(@PathVariable("question_id") Long question_id){
        return QQS.deleteAQuizQuestion(question_id);
    }

}
