package com.example.playbookProjApplicationBackend.Player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/answers")
@AllArgsConstructor
public class PlayerAnswerController {
    private PlayerAnswerService PAS;

    @GetMapping(path = "/player/count/{team_id}/{player_id}")
    public String getPlayerAverageAnswerSpeed(@PathVariable("team_id")Long id,@PathVariable("player_id") String player_id){
        return PAS.getPlayerAverageAnswerSpeed(id,player_id);
    }
    @PostMapping(path = "/new/{team_id}")
    public String uploadPlayerAnswer(@PathVariable("team_id") Long id,@RequestBody Map<String, Object> data) {
        return PAS.uploadPlayerAnswer(id, data);
    }
}
