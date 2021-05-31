package com.example.playbookProjApplicationBackend.Coach;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

@Service
public class CoachService {

    private CoachRepository CR;

    @Autowired
    public CoachService(CoachRepository CR) {
        this.CR = CR;
    }


    public String getCoach(Long coach_id){
        String response;
        if(!(doesCoachExist(coach_id))){
            response = new ResponseError("This coach does not exists",HttpStatus.BAD_REQUEST.value()).toJson();
            return response;
        }

       Coach foundCoach = CR.getOne(coach_id);
        response = new ResponseError(foundCoach.toJSONObj(),HttpStatus.OK.value()).toJson();
        return response;
    }
    @Transactional
    public String updateCoach(Long coach_id,Map<String,Object> coachUpdates){
        try{
            Coach coach = CR.getOne(coach_id);
            Team coachesTeam = coach.getTeam();
            if (coachUpdates.containsKey("email")) {
                for(Coach coaches: coachesTeam.getCoaches()){
                    if (coaches.getId() != coach_id  && coaches.getEmail().equals((String) coachUpdates.get("email"))){
                        return new ResponseError("caoch with with email " + coaches.getEmail() + " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
                    }
                }
            }
            coach.setFirstName(coachUpdates.containsKey("first_name") ? (String) coachUpdates.get("first_name") : coach.getFirstName());
            coach.setLastName(coachUpdates.containsKey("last_name") ? (String) coachUpdates.get("last_name") : coach.getLastName());
            coach.setEmail(coachUpdates.containsKey("email") ? (String) coachUpdates.get("email") : coach.getLastName());
            if(coachUpdates.containsKey("positions")){
                updateCoachPositions(coach, coachesTeam,(ArrayList<String>) coachUpdates.get("positions"));
            }
            return new ResponseError(coach.toJSONObj(), HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String deleteCoach(Long coach_id){
        if(!doesCoachExist(coach_id)){
            return new ResponseError("Coach with id " + coach_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            Coach coach = CR.getOne(coach_id);
            CR.removeCoach(coach.getId());
            return new ResponseError(coach.getId(), HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    private void updateCoachPositions(Coach coach, Team coachesTeam,ArrayList<String> positions){
        Set<Position> coachesPositions = coach.getPositions();
        coachesPositions.clear();
        for (Position position : coachesTeam.getPositions()){
            positions.forEach(pos -> {if(position.getPosition().equals(pos)){
                coachesPositions.add(position);
            }
            });
        }
        coach.setPositions(coachesPositions);
    }


    public boolean doesCoachExist(Long coach_id){
        return CR.findById(coach_id).isPresent();
    }
}
