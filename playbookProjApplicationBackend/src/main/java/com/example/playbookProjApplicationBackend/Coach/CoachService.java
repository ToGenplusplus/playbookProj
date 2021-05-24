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

    public String getAllCoachesInTeam(Long teamId){
        return new ResponseError(jsonify(CR.getCoachesByTeamId(teamId)),HttpStatus.OK.value()).toJson();
    }

    public String getCoachesByPosition(Long teamId, String posId){
        return new ResponseError(jsonify(CR.getCoachesByCoachPosition(teamId, posId)),HttpStatus.OK.value()).toJson();
    }

    public String getCoach(Long coach_id){
        //check if coach exist, if it does return coach else return exception
        String response;
        if(!(doesCoachExist(coach_id))){
            response = new ResponseError("This player does not exists",HttpStatus.BAD_REQUEST.value()).toJson();
            return response;
        }

       Coach foundCoach = CR.getOne(coach_id);
        response = new ResponseError(foundCoach.toJSONObj(),HttpStatus.OK.value()).toJson();
        return response;
    }
    @Transactional
    public String addNewCoach(Map<String,Object> coachObj){
        ResponseError resp=null;
        if(!coachObj.containsKey("email") || !coachObj.containsKey("first_name") || !coachObj.containsKey("last_name") || !coachObj.containsKey("team_id") || !coachObj.containsKey("positions")){
            return new ResponseError("invalid request, missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        Integer id = (Integer) coachObj.get("team_id");
        long team_id = id;
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("team with id " + team_id+ " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        String email = (String) coachObj.get("email");
        if(CR.getCoachByEmail(team_id,email) != null){
            return new ResponseError("Coach with email " + email+ " already exist for this team", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        ArrayList<String> coachPositions = (ArrayList<String>) coachObj.get("positions");
        for(String pos : coachPositions){
            if(!PosR.existsById(pos)){
                return new ResponseError("pos " + pos + " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
            }
        }
        try{
            Team team = TR.getOne(team_id);
            String first = (String) coachObj.get("first_name");
            String last = (String) coachObj.get("last_name");
            Coach coach = new Coach(first,last,email,team);
            CR.save(coach);
            for(String pos : coachPositions){
                CR.insertNewCoachPosition(coach.getId(),pos);
            }
            resp = new ResponseError("Success",HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }

    }
    @Transactional
    public String updateCoach(Long coach_id,Map<String,Object> coachUpdates){
        ResponseError resp=null;
        if(!doesCoachExist(coach_id)){
            return new ResponseError("Coach with id " + coach_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            Coach coach = CR.getOne(coach_id);
            coach.setFirstName(coachUpdates.get("first_name") == null? coach.getFirstName() : (String) coachUpdates.get("first_name"));
            coach.setLastName(coachUpdates.get("last_name") == null? coach.getLastName() : (String) coachUpdates.get("last_name"));
            coach.setEmail(coachUpdates.get("email") == null? coach.getEmail() : CR.getCoachByEmail(coach.getTeam().getId(),(String) coachUpdates.get("email")) == null ? (String) coachUpdates.get("email"): coach.getEmail());
            resp = new ResponseError("Success", HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
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
