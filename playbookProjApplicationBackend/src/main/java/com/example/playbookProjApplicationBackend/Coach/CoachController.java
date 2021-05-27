package com.example.playbookProjApplicationBackend.Coach;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/coaches")
@AllArgsConstructor
public class CoachController {

    private CoachService CS;
    
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
