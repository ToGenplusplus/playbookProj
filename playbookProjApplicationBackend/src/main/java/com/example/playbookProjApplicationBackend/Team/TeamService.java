package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Organization.OrganizationRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class TeamService {

    private TeamRepository TR;
    private OrganizationRepository OR;

    @Autowired
    public TeamService(TeamRepository TR, OrganizationRepository OR) {
        this.TR = TR;
        this.OR = OR;
    }

    public String getTeamById(Long org_id,Long team_id){return processResponse(org_id,team_id, "getTeamById"); }
    public String getTeamByName(Long org_id,String team_name){return processResponse(org_id,team_name, "getTeamByName"); }
    public String getTeamsInOrganization(Long org_id){return processResponse(org_id,null, "getTeamsInOrganization");}
    //delete Team
    //update Team

    private String processResponse(Long org_id,Object param, String methodName){
        ResponseError resp;
        if(!doesOrganizationExist(org_id)){
            resp = new ResponseError("organiztion with id: " + String.valueOf(org_id) + " does not exist",404);
        }else{
            resp = callMethod(org_id,param,methodName);
        }
        return resp.toJson();
    }

    private ResponseError callMethod(Long org_id,Object param, String methodName){
        ResponseError resp;
        switch (methodName){
            case "getTeamById":
                Team teamId = TR.findTeamById(org_id,(Long) param);
                resp = teamId == null ? new ResponseError("Team with id " + String.valueOf(param) + " does not exist",400) :
                        new ResponseError(TR.findTeamById(org_id,(Long) param).toJSONObj(),200);
                break;
            case "getTeamByName":
                Team team = TR.findTeamByName(org_id,(String) param);
                if (team != null)
                    resp = new ResponseError(team.toJSONObj(),200);
                else
                    resp = new ResponseError("Team with name " + param + " does not exist",400);
                break;
            case "getTeamsInOrganization":
                resp = new ResponseError(jsonify(TR.getTeamsInOrganization(org_id)),200);
                break;
            default:
                resp = new ResponseError("Invalid request",404);
        }
        return resp;
    }

    private boolean doesOrganizationExist(Long org_id){
        return OR.findById(org_id).isPresent();
    }

    private JSONObject jsonify(Collection<Team> teams){
        JSONObject teamObj  = new JSONObject();
        JSONArray teamsArray = new JSONArray();
        for (Team team: teams){
            teamsArray.add(team.toJSONObj());
        }

        teamObj.put("teams",teamsArray);
        return teamObj;
    }
}
