package com.example.playbookProjApplicationBackend.Coach;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/coaches")
@AllArgsConstructor
public class CoachController {

    private CoachService CS;

    @GetMapping(path = "{id}")
    public String getAllCoachesInTeam(@PathVariable("id")Long team_id) {
        return CS.getAllCoachesInTeam(team_id);
    }

    @GetMapping(path = "/{id}/{position}")
    public String getCoachesByPosition(@PathVariable("id")Long team_id, @PathVariable("position")String position_id) {
        return CS.getCoachesByPosition(team_id,position_id);
    }

    @GetMapping(path = "/coach/{id}")
    public String getCoach(@PathVariable("id")Long coach_id){
        return CS.getCoach(coach_id);
    }


}
