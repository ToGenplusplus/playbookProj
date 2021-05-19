package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.PlayerRepository;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuizQuestionService {

    private QuizQuestionRepository QR;
    private TeamRepository TR;
    private PlayerRepository PR;
    private PositionRepository PosR;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository QR, TeamRepository TR, PlayerRepository PR,PositionRepository PosR) {
        this.QR = QR;
        this.TR = TR;
        this.PR = PR;
        this.PosR = PosR;
    }

    public String getAllQuizQuestionsInDatabase(){
        return new ResponseError(jsonify(QR.findAll()),200).toJson();
    }
    public String getAllQuizQuestionsForTeam(Long team_id){
        return processResponse(team_id,null,"getAllQuizQuestionsForTeam",false);
    }
    public String getAllQuestionsForTeamRandom(Long team_id){
        return processResponse(team_id,null,"getAllQuestionsForTeamRandom",false);
    }
    public String getAllQuestionsForTeamByPosition(Long team_id, String position){
        return processResponse(team_id,position,"getAllQuestionsForTeamByPosition",false);
    }
    public String getAllQuestionsForTeamByPositionRandom(Long team_id, String position){
        return processResponse(team_id,position,"getAllQuestionsForTeamByPositionRandom",false);
    }
    public String getAllAnsweredQuestionsForTeam(Long team_id){
        return processResponse(team_id,null,"getAllAnsweredQuestionsForTeam",false);
    }
    public String getCountAllAnsweredQuestionsForTeam(Long team_id){
        return processResponse(team_id,null,"getCountAllAnsweredQuestionsForTeam",false);
    }
    public String getAllAnsweredQuestionsForTeamByPlayer(Long team_id, String player_id){
        return processResponse(team_id,player_id,"getAllAnsweredQuestionsForTeamByPlayer",true);
    }
    public String getCountAnsweredQuestionsForTeamByCategory(Long team_id, String category){
        return processResponse(team_id,category,"getCountAnsweredQuestionsForTeamByCategory",false);
    }

    private String processResponse(Long team_id, String arg,String methodToCall, boolean argIsPlayer){
        ResponseError resp;
        boolean exists;
        String errMessage;
        if (!(arg == null)){
            Map<String,Boolean> posorplay = PositionOrPlayer( arg,argIsPlayer);
            exists = posorplay.get("exists");
            errMessage = argIsPlayer ? "Player " +arg+ " does not exist" : "Position " +arg+ " does not exist";
        }else{
            exists = true;
            errMessage = "";
        }

        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team with id: " +String.valueOf(team_id) + " does not exist",404);
        }else if(!exists){
            resp = new ResponseError(errMessage,404);
        }else{
            resp = callMethod(team_id,arg,methodToCall);
        }

        return resp.toJson();
    }

    private ResponseError callMethod(Long team_id, String arg, String methodToCall){
        ResponseError resp;
        switch (methodToCall) {
            case "getAllQuizQuestionsForTeam":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeam(team_id)),200);
                break;
            case "getAllQuestionsForTeamRandom":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamRandom(team_id)),200);
                break;
            case "getAllQuestionsForTeamByPosition":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamByPosition(team_id,arg)),200);
                break;
            case "getAllQuestionsForTeamByPositionRandom":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamByPositionRandom(team_id,arg)),200);
                break;
            case "getAllAnsweredQuestionsForTeam":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForTeam(team_id)),200);
                break;
            case "getCountAllAnsweredQuestionsForTeam":
                int count = QR.getAllAnsweredQuestionsForTeam(team_id).size();
                resp = new ResponseError(count,200);
                break;
            case "getAllAnsweredQuestionsForTeamByPlayer":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForTeamByPlayer(team_id,arg)),200);
                break;
            case "getCountAnsweredQuestionsForTeamByCategory":
                resp = new ResponseError(QR.getCountAnsweredQuestionsForTeamByCategory(team_id,arg),200);
                break;
            default:
                resp = new ResponseError("Invalid request",404);
        }
        return resp;
    }

    private boolean doesTeamExist(Long id){
        return TR.findById(id).isPresent();
    }

    private boolean doesPlayerExist(String player_id){
        return PR.findById(player_id).isPresent();
    }
    private boolean doesPositionExist(String pos_id){
        return PosR.findById(pos_id).isPresent();
    }

    private Map<String,Boolean> PositionOrPlayer(String arg, boolean isPlayer){
        Map<String,Boolean> ret = new HashMap<>();
        if (isPlayer){
            ret.put("exists",doesPlayerExist(arg));
        }else{
            ret.put("exists",doesPositionExist(arg));
        }
        return  ret;
    }


    private JSONObject jsonify(Collection<QuizQuestion> questions){
        JSONObject questionObject = new JSONObject();
        JSONArray questionArray = new JSONArray();
        for (QuizQuestion quest : questions){
            questionArray.add(quest.toJSONObj());
        }

        questionObject.put("questions",questionArray);
        return questionObject;
    }
}
