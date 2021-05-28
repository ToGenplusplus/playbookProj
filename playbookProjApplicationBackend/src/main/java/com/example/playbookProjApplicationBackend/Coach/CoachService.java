package com.example.playbookProjApplicationBackend.Coach;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Team.Team;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
public class CoachService {

    private CoachRepository CR;
    private TeamRepository TR;
    private PositionRepository PosR;

    @Autowired
    public CoachService(CoachRepository CR, TeamRepository TR, PositionRepository PosR) {
        this.CR = CR;
        this.TR = TR;
        this.PosR = PosR;
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
            return new ResponseError("Success", HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String deleteCoach(Long coach_id){
        ResponseError resp=null;
        if(!doesCoachExist(coach_id)){
            return new ResponseError("Coach with id " + coach_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            CR.deleteById(coach_id);
            resp = new ResponseError("Success", HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
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

    public JSONObject jsonify(Collection<Coach> coaches){
        JSONObject coachObject = new JSONObject();
        JSONArray coachesArray = new JSONArray();
        for (Coach coach : coaches){
            coachesArray.add(coach.toJSONObj());
        }

        coachObject.put("coaches",coachesArray);
        return coachObject;

    }

    public boolean doesCoachExist(Long coach_id){
        return CR.findById(coach_id).isPresent();
    }
}
