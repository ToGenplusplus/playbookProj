package com.example.playbookProjApplicationBackend.Coach;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
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
        if(!doesCoachExist(coach_id)){
            return new ResponseError("Coach with id " + coach_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        Coach coach = CR.getOne(coach_id);
        String email;
        if (coachUpdates.containsKey("email")){
            String objemail = (String) coachUpdates.get("email");
            if(CR.getCoachByEmail(coach.getTeam().getId(),objemail) != null && !coach.getEmail().equals(objemail)){
                return new ResponseError("coach with with email " + objemail+ " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
            }
            email = objemail;
        }else{
            email = coach.getEmail();
        }
        try{
            coach.setFirstName(coachUpdates.containsKey("first_name") ? (String) coachUpdates.get("first_name") : coach.getFirstName());
            coach.setLastName(coachUpdates.containsKey("last_name") ? (String) coachUpdates.get("last_name") : coach.getLastName());
            coach.setEmail(email);
            if(coachUpdates.get("positions") != null){
                updateCoachPositions(coach_id,(ArrayList<String>) coachUpdates.get("positions"));
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
    private void updateCoachPositions(Long coach_id,ArrayList<String> positions){
        if (positions.size() == 0){
            return;
        }
        CR.removeCoachPosition(coach_id);
        for (String position: positions){
            if(PosR.existsById(position)){
                CR.insertNewCoachPosition(coach_id,position);
            }
        }
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
