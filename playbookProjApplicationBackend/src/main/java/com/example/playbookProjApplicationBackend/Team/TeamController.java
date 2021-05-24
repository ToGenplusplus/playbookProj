package com.example.playbookProjApplicationBackend.Team;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/team")
@AllArgsConstructor
public class TeamController {

    private TeamService TS;

    @GetMapping(path = "/all/{org_id}")
    public String getTeamsInOrganization(@PathVariable("org_id")Long org_id){return TS.getTeamsInOrganization(org_id); }
    @GetMapping(path = "/byId/{org_id}/{team_id}")
    public String getTeamById(@PathVariable("org_id")Long org_id,@PathVariable("team_id")Long team_id){return TS.getTeamById(org_id,team_id);}
    @GetMapping(path = "/byName/{org_id}/{team_name}")
    public String getTeamByName(@PathVariable("org_id")Long org_id,@PathVariable("team_name")String team_name){return TS.getTeamByName(org_id,team_name); }
    @PutMapping(path = "/questions/deactivate/{org_id}/{team_id}")
    public String deactivateAllTeamQuestions(@PathVariable("org_id")Long org_id,@PathVariable("team_id")Long team_id){
        return TS.deactivateAllTeamQuestions(org_id,team_id);
    }
    @PutMapping(path = "/questions/deactivate/byPosition/{org_id}/{team_id}/{type}")
    public String deactivateAllTeamPositionQuestions(@PathVariable("org_id")Long org_id,@PathVariable("team_id")Long team_id,@PathVariable("type") String type){
        return  TS.deactivateAllTeamPositionQuestions(org_id, team_id, type);
    }
    @DeleteMapping(path = "/questions/delete/byPosition/{org_id}/{team_id}/{type}")
    public String deleteAllTeamPositionQuestions(@PathVariable("org_id")Long org_id,@PathVariable("team_id")Long team_id,@PathVariable("type") String type){
        return  TS.deleteAllTeamPositionQuestions(org_id, team_id, type);
    }
    @DeleteMapping(path = "/questions/delete/{org_id}/{team_id}")
    public String deleteAllTeamQuestions(@PathVariable("org_id")Long org_id,@PathVariable("team_id")Long team_id){
        return TS.deleteAllTeamQuestions(org_id,team_id);
    }
}
