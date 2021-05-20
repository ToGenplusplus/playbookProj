package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Organization.OrganizationRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public String deleteAllTeamQuestions(Long org_id,Long team_id){
        return processResponse(org_id,team_id,"deleteAllTeamQuestions");
    }
    //update Team

    private String processResponse(Long org_id,Object param, String methodName){
        ResponseError resp = null;
        try{
            if(!doesOrganizationExist(org_id)){
                resp = new ResponseError("organiztion with id: " + String.valueOf(org_id) + " does not exist", HttpStatus.BAD_REQUEST.value());
            }else{
                resp = callMethod(org_id,param,methodName);
            }
        }catch(Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }

    private ResponseError callMethod(Long org_id,Object param, String methodName){
        ResponseError resp;
        switch (methodName){
            case "getTeamById":
                Team teamId = TR.findTeamById(org_id,(Long) param);
                resp = teamId == null ? new ResponseError("Team with id " + String.valueOf(param) + " does not exist",HttpStatus.BAD_REQUEST.value()) :
                        new ResponseError(TR.findTeamById(org_id,(Long) param).toJSONObj(),HttpStatus.OK.value());
                break;
            case "getTeamByName":
                Team team = TR.findTeamByName(org_id,(String) param);
                if (team != null)
                    resp = new ResponseError(team.toJSONObj(),HttpStatus.OK.value());
                else
                    resp = new ResponseError("Team with name " + param + " does not exist",HttpStatus.BAD_REQUEST.value());
                break;
            case "getTeamsInOrganization":
                resp = new ResponseError(jsonify(TR.getTeamsInOrganization(org_id)),HttpStatus.OK.value());
                break;
            case "deleteAllTeamQuestions":
                Team q = TR.findTeamById(org_id,(Long) param);
                resp = q == null ? new ResponseError("Team with id " + String.valueOf(param) + " does not exist",HttpStatus.BAD_REQUEST.value()) :
                        new ResponseError("Sucess",HttpStatus.OK.value());
                TR.deleteAllTeamQuestions(org_id,(Long) param);
                break;
            default:
                resp = new ResponseError("Invalid request",HttpStatus.BAD_REQUEST.value());
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
