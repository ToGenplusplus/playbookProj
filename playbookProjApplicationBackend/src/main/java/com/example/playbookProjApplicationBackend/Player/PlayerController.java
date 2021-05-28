package com.example.playbookProjApplicationBackend.Player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/players")
@AllArgsConstructor
public class PlayerController {

    private PlayerService PS;


    @GetMapping(path = "/player/{player_id}")
    public String getPlayer(@PathVariable("player_id")String player_id){
        return PS.getPlayer(player_id);
    }
    @PostMapping(path = "/player-answer/new")
    public String addNewPlayerAnswer(@RequestBody Map<String, Object> data){
        return PS.addNewPlayerAnswer(data);
    }
    @PutMapping(path = "/player/update/{player_id}" )
    public String updatePlayer(@PathVariable("player_id") String player_id,@RequestBody Map<String, Object> updates){
        return PS.updatePlayer(player_id, updates);
    }
    @DeleteMapping(path = "/player/delete/{player_id}")
    public String deletePlayer(@PathVariable("player_id") String player_id){
        return PS.deletePlayer(player_id);
    }
}
