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
    private ResponseError responseError;

    @Autowired
    public CoachService(CoachRepository CR, ResponseError responseError) {
        this.CR = CR;
        this.responseError = responseError;
    }


    public String getCoach(Long coach_id){
        String response;
        if(!(doesCoachExist(coach_id))){
            response = sendResponse("This coach does not exists",HttpStatus.BAD_REQUEST.value());
            return response;
        }

       Coach foundCoach = CR.getOne(coach_id);
        response = sendResponse(foundCoach.toJSONObj(),HttpStatus.OK.value());
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
                        return sendResponse("caoch with with email " + coaches.getEmail() + " already exists", HttpStatus.BAD_REQUEST.value());
                    }
                }
            }
            coach.setFirstName(coachUpdates.containsKey("first_name") ? (String) coachUpdates.get("first_name") : coach.getFirstName());
            coach.setLastName(coachUpdates.containsKey("last_name") ? (String) coachUpdates.get("last_name") : coach.getLastName());
            coach.setEmail(coachUpdates.containsKey("email") ? (String) coachUpdates.get("email") : coach.getLastName());
            if(coachUpdates.containsKey("positions")){
                updateCoachPositions(coach, coachesTeam,(ArrayList<String>) coachUpdates.get("positions"));
            }
            return sendResponse(coach.toJSONObj(), HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Transactional
    public String deleteCoach(Long coach_id){
        if(!doesCoachExist(coach_id)){
            return sendResponse("Coach with id " + coach_id+ " does not exists", HttpStatus.BAD_REQUEST.value());
        }
        try{
            Coach coach = CR.getOne(coach_id);
            CR.removeCoach(coach.getId());
            return sendResponse(coach.getId(), HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
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

    private String sendResponse(Object Message, int errorCode){
        responseError.setMessage(Message);
        responseError.setErrorCode(errorCode);
        return responseError.toJson();
    }
}
