package com.example.playbookProjApplicationBackend.Team;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/team")
@AllArgsConstructor
public class TeamController {

    private TeamService TS;

    @GetMapping(path = "/all/quiz/{team_id}")
    public String getAllQuizzesInTeam(@PathVariable("team_id")Long team_id){return TS.getAllQuizzesInTeam(team_id); }
    @GetMapping(path = "/all/quiz/{team_id}/{position_id}")
    public String getAllQuizzesForATeamPosition(@PathVariable("team_id")Long team_id, @PathVariable("position_id") String position_id){return TS.getAllQuizzesForATeamPosition(team_id, position_id); }
    @GetMapping(path = "/all/quiz/player/{team_id}/{player_id}")
    public String getAllQuizzesForATeamsPlayer(@PathVariable("team_id") Long team_id,@PathVariable("player_id") String player_id){return TS.getAllQuizzesForATeamsPlayer(team_id,player_id); }
}
