package com.example.playbookProjApplicationBackend.Player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/players")
@AllArgsConstructor
public class PlayerController {

    private PlayerService PS;

    @GetMapping(path = "{team_id}")
    public String getAllPlayersInTeam(@PathVariable("team_id")Long team_id) {
        return PS.getAllPlayersInTeam(team_id);
    }

    @GetMapping(path = "/{team_id}/{position_id}")
    public String getAllPlayersInAPosition(@PathVariable("team_id")Long team_id, @PathVariable("position_id")String position_id) {
        return PS.getAllPlayersInPosition(team_id,position_id);
    }
    @GetMapping(path = "/player/{player_id}")
    public String getPlayer(@PathVariable("player_id")String player_id){
        return PS.getPlayer(player_id);
    }
    @PostMapping(path ="/new")
    public String addNewPlayer(@RequestBody Map<String,Object> player){
        return PS.addNewPlayer(player);
    }
    @PutMapping(path = "/update/{player_id}" )
    public String updatePlayer(@PathVariable("player_id") String player_id,@RequestBody Map<String, Object> updates){
        return PS.updatePlayer(player_id, updates);
    }
}
