package com.example.playbookProjApplicationBackend.Player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/answers")
@AllArgsConstructor
public class PlayerAnswerController {
    private PlayerAnswerService PAS;

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
    @PostMapping(path = "/new/{team_id}")
    public String uploadPlayerAnswer(@PathVariable("team_id") Long id,@RequestBody Map<String, Object> data) {
        return PAS.uploadPlayerAnswer(id, data);
    }
}
