package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Organization.OrganizationRepository;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Quiz.Quiz;
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
    private PositionRepository PR;

    @Autowired
    public TeamService(TeamRepository TR, OrganizationRepository OR, PositionRepository PR) {
        this.TR = TR;
        this.OR = OR;
        this.PR = PR;
    }

    public String getAllQuizzesInTeam(Long team_id){
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("Team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Team team = TR.getOne(team_id);
            return new ResponseError(jsonifyQuizzes(team.getQuizzes()),HttpStatus.OK.value()).toJson();

        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllQuizzesForATeamPosition(Long team_id, String position_id){
        if(!TR.findById(team_id).isPresent() || !doesPositionExist(position_id)){
            return new ResponseError("invalid request, make sure team and position exists",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Collection<Quiz> positionQuizzes = TR.getOne(team_id).getQuizzes();
            positionQuizzes.removeIf(quiz -> !quiz.getPosition().getPosition().equals(position_id));
            return new ResponseError(jsonifyQuizzes(positionQuizzes),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllQuizzesForATeamsPlayer(Long team_id, String player_id){
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("invalid request, make sure team and position exists",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Team team = TR.getOne(team_id);
            Player player = team.getPlayers().
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getTeamById(Long org_id,Long team_id){return processResponse(org_id,team_id, "getTeamById"); }
    public String getTeamByName(Long org_id,String team_name){return processResponse(org_id,team_name, "getTeamByName"); }
    public String getTeamsInOrganization(Long org_id){return processResponse(org_id,null, "getTeamsInOrganization");}
    //delete Team
    //update Team

    private String processResponse(Long org_id,Object param, String methodName){
        try{
            if(!doesOrganizationExist(org_id)){
                return new ResponseError("organiztion with id: " + String.valueOf(org_id) + " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
            }else{
                return callMethod(org_id,param,methodName).toJson();
            }
        }catch(Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }

    private ResponseError callMethod(Long org_id,Object param, String methodName){
        ResponseError resp;
        Team team;
        switch (methodName){
            case "getTeamById":
                Team teamId = TR.findTeamById(org_id,(Long) param);
                resp = teamId == null ? new ResponseError("Team with id " + String.valueOf(param) + " does not exist",HttpStatus.BAD_REQUEST.value()) :
                        new ResponseError(TR.findTeamById(org_id,(Long) param).toJSONObj(),HttpStatus.OK.value());
                break;
            case "getTeamByName":
                team = TR.findTeamByName(org_id,(String) param);
                if (team != null)
                    resp = new ResponseError(team.toJSONObj(),HttpStatus.OK.value());
                else
                    resp = new ResponseError("Team with name " + param + " does not exist",HttpStatus.BAD_REQUEST.value());
                break;
            case "getTeamsInOrganization":
                resp = new ResponseError(jsonify(TR.getTeamsInOrganization(org_id)),HttpStatus.OK.value());
                break;
            default:
                resp = new ResponseError("Invalid request",HttpStatus.BAD_REQUEST.value());
        }
        return resp;
    }

    private boolean doesOrganizationExist(Long org_id){
        return OR.findById(org_id).isPresent();
    }
    private boolean doesPositionExist(String pos){
        return PR.findById(pos).isPresent();
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
    private JSONObject jsonifyQuizzes(Collection<Quiz> quizzes){
        JSONObject quizObj  = new JSONObject();
        JSONArray quizArray = new JSONArray();
        for (Quiz quiz: quizzes){
            quizArray.add(quiz.toJSONObj());
        }

        quizObj.put("quizzes",quizArray);
        return quizObj;
    }
}
