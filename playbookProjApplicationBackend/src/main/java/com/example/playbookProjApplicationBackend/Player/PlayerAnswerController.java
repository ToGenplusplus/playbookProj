package com.example.playbookProjApplicationBackend.Player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/answers")
@AllArgsConstructor
public class PlayerAnswerController {
    private PlayerAnswerService PAS;

    @GetMapping(path = "/all/{team_id}/{type_id}")
    public String getAllPlayerAnswersInTeamByQuestionType(@PathVariable("team_id")Long id,@PathVariable("type_id") String type){
        return PAS.getAllPlayerAnswersInTeamByQuestionType(id,type);
    }
    @GetMapping(path = "/position/{team_id}/{position_id}")
    public String getAllPlayerAnswersInTeamByPosition(@PathVariable("team_id")Long id,@PathVariable("position_id")  String position){
        return PAS.getAllPlayerAnswersInTeamByPosition(id,position);
    }
    @GetMapping(path = "/position/{team_id}/{position_id}/player/{player_id}")
    public String getAllPlayerAnswersInTeamByPositionByPlayer(@PathVariable("team_id")Long id,@PathVariable("position_id") String position,@PathVariable("player_id") String player_id){
        return PAS.getAllPlayerAnswersInTeamByPositionByPlayer(id,position,player_id);
    }
    @GetMapping(path = "/category/count/{team_id}/{type_id}")
    public String getTotalAverageAnswerSpeedForQuizCategory(@PathVariable("team_id")Long id,@PathVariable("type_id")  String type){
        return PAS.getTotalAverageAnswerSpeedForQuizCategory(id,type);
    }
    @GetMapping(path = "/player/count/{team_id}/{player_id}")
    public String getPlayerAverageAnswerSpeed(@PathVariable("team_id")Long id,@PathVariable("player_id") String player_id){
        return PAS.getPlayerAverageAnswerSpeed(id,player_id);
    }
    @GetMapping(path = "/player/question/count/{team_id}/{player_id}/{question_id}")
    public String getCountPlayerAnswerForQuestion(@PathVariable("team_id")Long id,@PathVariable("player_id") String player_id,@PathVariable("question_id")Long question_id){
        return PAS.getCountPlayerAnswerForQuestion(id,player_id,question_id);
    }
    @GetMapping(path = "/position/count/{team_id}/{position_id}")
    public String getTotalNumberOfPlayerAnswerForPosition(@PathVariable("team_id")Long id,@PathVariable("position_id") String position){
       return PAS.getTotalNumberOfPlayerAnswerForPosition(id,position);
    }
}
