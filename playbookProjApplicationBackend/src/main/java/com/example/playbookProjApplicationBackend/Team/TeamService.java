package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private TeamRepository TR;

    @Autowired
    public TeamService(TeamRepository TR) {
        this.TR = TR;
    }

    public String getTeamById(Long team_id){return callMethod(team_id, "getTeamById"); }
    public String getTeamByName(String team_name){return callMethod(team_name, "getTeamByName"); }
    //delete Team
    //update Team

    private String callMethod(Object param, String methodName){
        ResponseError resp;
        switch (methodName){
            case "getTeamById":
                boolean exists = TR.findById((Long) param).isPresent();
                if (exists)
                    resp = new ResponseError(TR.getOne((Long) param).toJSONObj(),200);
                else
                    resp = new ResponseError("Team with id " + String.valueOf(param) + " does not exist",400);
                break;
            case "getTeamByName":
                Team team = TR.findTeamByName((String) param);
                if (team != null)
                    resp = new ResponseError(team.toJSONObj(),200);
                else
                    resp = new ResponseError("Team with name " + param + " does not exist",400);
                break;
            default:
                resp = new ResponseError("Invalid request",404);
        }
        return resp.toJson();
    }
}
