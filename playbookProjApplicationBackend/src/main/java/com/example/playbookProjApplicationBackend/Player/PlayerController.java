package com.example.playbookProjApplicationBackend.Player;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/players")
@AllArgsConstructor
public class PlayerController {

    private PlayerService PS;

    @GetMapping(path = "{id}")
    public String getAllPlayersInTeam(@PathVariable("id")Long team_id) {
        return PS.getAllPlayersInTeam(team_id);
    }

    @GetMapping(path = "/{id}/{position}")
    public String getAllPlayersInAPosition(@PathVariable("id")Long team_id, @PathVariable("position")String position_id) {
        return PS.getAllPlayersInPosition(team_id,position_id);
    }

    @GetMapping(path = "/player/{id}")
    public String getPlayer(@PathVariable("id")String player_id){
        return PS.getPlayer(player_id);
    }
}
