package com.example.playbookProjApplicationBackend.Team;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/team")
@AllArgsConstructor
public class TeamController {

    private TeamService TS;

    @GetMapping(path = "/byId/{team_id}")
    public String getTeamById(@PathVariable("team_id")Long team_id){return TS.getTeamById(team_id);}
    @GetMapping(path = "/byName/{team_name}")
    public String getTeamByName(@PathVariable("team_name")String team_name){return TS.getTeamByName(team_name); }
}
