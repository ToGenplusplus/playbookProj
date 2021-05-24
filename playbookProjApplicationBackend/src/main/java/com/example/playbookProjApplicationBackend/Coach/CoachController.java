package com.example.playbookProjApplicationBackend.Coach;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
    @PostMapping(path = "/new")
    public String addNewCoach(@RequestBody Map<String,Object> coachObj){
        return CS.addNewCoach(coachObj);
    }
    @PutMapping(path = "/update/{coach_id}")
    public String updateCoach(@PathVariable("coach_id") Long coach_id,@RequestBody Map<String,Object> coachObj){
        return CS.updateCoach(coach_id,coachObj);
    }
    @DeleteMapping(path = "/delete/{coach_id}")
    public String deleteCoach(@PathVariable("coach_id") Long id){
        return CS.deleteCoach(id);
    }

}
