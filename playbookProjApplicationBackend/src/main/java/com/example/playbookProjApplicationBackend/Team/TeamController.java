package com.example.playbookProjApplicationBackend.Team;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/team")
@AllArgsConstructor
public class TeamController {

    private TeamService TS;

    @GetMapping(path = "/coach/all/{team_id}")
    public String getAllCoachesInTeam(@PathVariable("team_id")Long team_id) {
        return TS.getAllCoachesInTeam(team_id);
    }
    @GetMapping(path = "/coach/all/positions/{team_id}/{position_id}")
    public String getCoachesByPosition(@PathVariable("team_id")Long team_id, @PathVariable("position_id")String position_id) {
        return TS.getCoachesByPosition(team_id,position_id);
    }
    @GetMapping(path = "/player/all/{team_id}")
    public String getAllPlayersInTeam(@PathVariable("team_id")Long team_id) {
        return TS.getAllPlayersInTeam(team_id);
    }

    @GetMapping(path = "/player/all/positions/{team_id}/{position_id}")
    public String getAllPlayersInATeamsPosition(@PathVariable("team_id")Long team_id, @PathVariable("position_id")String position_id) {
        return TS.getAllPlayersInATeamsPosition(team_id,position_id);
    }
    @GetMapping(path = "/all/quiz/{team_id}")
    public String getAllQuizzesInTeam(@PathVariable("team_id")Long team_id){return TS.getAllQuizzesInTeam(team_id); }
    @GetMapping(path = "/all/quiz/{team_id}/{position_id}")
    public String getAllQuizzesForATeamPosition(@PathVariable("team_id")Long team_id, @PathVariable("position_id") String position_id){return TS.getAllQuizzesForATeamPosition(team_id, position_id); }
    @GetMapping(path = "/all/quiz/player/{team_id}/{player_id}")
    public String getAllQuizzesForATeamsPlayer(@PathVariable("team_id") Long team_id,@PathVariable("player_id") String player_id){return TS.getAllQuizzesForATeamsPlayer(team_id,player_id); }
    @PostMapping(path = "/player/new")
    public String addNewPlayer(@RequestBody Map<String,Object> newPlayer){
        return TS.addNewPlayer(newPlayer);
    }
}
