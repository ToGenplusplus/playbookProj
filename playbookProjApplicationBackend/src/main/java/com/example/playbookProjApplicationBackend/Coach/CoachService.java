package com.example.playbookProjApplicationBackend.Coach;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CoachService {

    private CoachRepository CR;

    @Autowired
    public CoachService(CoachRepository CR) {
        this.CR = CR;
    }

    public String getAllCoachesInTeam(Long teamId){
        return new ResponseError(jsonify(CR.getCoachesByTeamId(teamId)),200).toJson();
    }

    public String getCoachesByPosition(Long teamId, String posId){
        return new ResponseError(jsonify(CR.getCoachesByCoachPosition(teamId, posId)),200).toJson();
    }

    public String getCoach(Long coach_id){
        //check if coach exist, if it does return coach else return exception
        String response;
        if(!(doesCoachExist(coach_id))){
            response = new ResponseError("This player does not exists",404).toJson();
            return response;
        }

       Coach foundCoach = CR.getOne(coach_id);
        response = new ResponseError(foundCoach.toJSONObj(),200).toJson();
        return response;
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
