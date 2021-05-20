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

    @GetMapping(path = "/all/{team_id}")
    public String getAllQuizQuestionsForTeam(@PathVariable("team_id")Long id){
        return QQS.getAllQuizQuestionsForTeam(id);
    }
    @GetMapping(path = "/all/{team_id}/rand")
    public String getAllQuestionsForTeamRandom(@PathVariable("team_id")Long id){
        return QQS.getAllQuestionsForTeamRandom(id);
    }
    @GetMapping(path = "/all/{team_id}/{position_id}")
    public String getAllQuestionsForTeamByPosition(@PathVariable("team_id")Long id,@PathVariable("position_id") String position) {
        return QQS.getAllQuestionsForTeamByPosition(id, position);
    }
    @GetMapping(path = "/all/{team_id}/{position_id}/rand")
    public String getAllQuestionsForTeamByPositionRandom(@PathVariable("team_id")Long id,@PathVariable("position_id") String position){
        return QQS.getAllQuestionsForTeamByPositionRandom(id,position);
    }
    @GetMapping(path = "/all/answered/{team_id}")
    public String getAllAnsweredQuestionsForTeam(@PathVariable("team_id")Long id){
        return QQS.getAllAnsweredQuestionsForTeam(id);
    }
    @GetMapping(path = "/all/answered/count/{team_id}")
    public String getCountAllAnsweredQuestionsForTeam(@PathVariable("team_id")Long id){
        return QQS.getCountAllAnsweredQuestionsForTeam(id);
    }
    @GetMapping(path = "/all/answered/{team_id}/{player_id}")
    public String getAllAnsweredQuestionsForTeamByPlayer(@PathVariable("team_id")Long id,@PathVariable("player_id") String player_id){
        return QQS.getAllAnsweredQuestionsForTeamByPlayer(id,player_id);
    }
    @GetMapping(path = "/all/answered/count/{team_id}/{position_id}")
    public String getCountAnsweredQuestionsForTeamByCategory(@PathVariable("team_id")Long id,@PathVariable("position_id") String category){
        return QQS.getCountAnsweredQuestionsForTeamByCategory(id,category);
    }

    @PostMapping(path = "/new")
    public String insertNewQuestion(@RequestBody Map<String,Object> newQuestion){
        return QQS.insertNewQuestion(newQuestion);
    }

}
