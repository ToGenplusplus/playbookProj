package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestionRepository;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlayerAnswerService {
    private PlayerAnswerRepository PAR;
    private PlayerRepository PR;
    private PositionRepository PosR;
    private TeamRepository TR;
    private QuizQuestionRepository QQR;

    @Autowired
    public PlayerAnswerService(PlayerAnswerRepository PAR, PlayerRepository PR,
                               PositionRepository posR, TeamRepository TR,
                               QuizQuestionRepository QQR) {
        this.PAR = PAR;
        this.PR = PR;
        this.PosR = posR;
        this.TR = TR;
        this.QQR = QQR;
    }

    public String getAllPlayerAnswersInTeamByQuestionType(Long id, String type){
        return processResponse(id,type,null,"getAllPlayerAnswersInTeamByQuestionType",true);
    }
    public String getAllPlayerAnswersInTeamByPosition(Long id, String position){
        return processResponse(id,position,null,"getAllPlayerAnswersInTeamByPosition",true);
    }
    public String getAllPlayerAnswersInTeamByPositionByPlayer(Long id, String position, String player_id){
        return processResponse(id,position,player_id,"getAllPlayerAnswersInTeamByPositionByPlayer",true);
    }
    //check
    public String getTotalAverageAnswerSpeedForQuizCategory(Long id, String type){
        return processResponse(id,type,null,"getTotalAverageAnswerSpeedForQuizCategory",true);
    }
    public String getPlayerAverageAnswerSpeed(Long id, String player_id){
        return processResponse(id,player_id,null,"getPlayerAverageAnswerSpeed",false);
    }
    public String getCountPlayerAnswerForQuestion(Long id, String player_id,Long question_id){
        return processResponse(id,player_id,question_id,"getCountPlayerAnswerForQuestion",false);
    }
    public String getTotalNumberOfPlayerAnswerForPosition(Long id, String position){
        return processResponse(id,position,null,"getTotalNumberOfPlayerAnswerForPosition",true);
    }


    private String processResponse(Long team_id, String arg,Object arg2, String methodToCall, boolean isPos){
        ResponseError resp;
        Map<String,Boolean> ret = PositionOrPlayer(arg,isPos);
        boolean exists = ret.get("exists");
        String errMessage = isPos ? "Position " +arg+ " does not exist" : "Player " +arg+ " does not exist";
        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team "+ String.valueOf(team_id) + " does not exist",404);
        }else if(!exists){
            resp = new ResponseError(errMessage,404);
        }else{
            resp = callMethod(team_id,arg,arg2,methodToCall);
        }
        return resp.toJson();
    }

    private ResponseError callMethod(Long team_id, String arg, Object arg2, String methodToCall){
        ResponseError resp;
        switch (methodToCall) {
            case "getAllPlayerAnswersInTeamByQuestionType":
                resp = new ResponseError(jsonify(PAR.getAllPlayerAnswersInTeamByQuestionType(team_id,arg)),200);
                break;
            case "getAllPlayerAnswersInTeamByPosition":
                resp = new ResponseError(jsonify(PAR.getAllPlayerAnswersInTeamByPosition(team_id,arg)),200);
                break;
            case "getAllPlayerAnswersInTeamByPositionByPlayer":
                String player_id = (String) arg2;
                if (doesPlayerExist(player_id))
                    resp = new ResponseError(jsonify(PAR.getAllPlayerAnswersInTeamByPositionByPlayer(team_id,arg,player_id)),200);
                else
                    resp = new ResponseError("Player " + player_id + " does not exist",404);
                break;
            case "getTotalAverageAnswerSpeedForQuizCategory":
                resp = new ResponseError(PAR.getTotalAverageAnswerSpeedForQuizCategory(team_id,arg),200);
                break;
            case "getPlayerAverageAnswerSpeed":
                resp = new ResponseError(PAR.getPlayerAverageAnswerSpeed(team_id,arg),200);
                break;
            case "getCountPlayerAnswerForQuestion":
                Long question_id = (Long) arg2;
                if(doesQuizQuestionExists(question_id))
                    resp = new ResponseError(PAR.getCountPlayerAnswerForQuestion(team_id,arg,(Long) arg2),200);
                else
                    resp = new ResponseError("Quiz question with id: " + question_id + " does not exist",404);
                break;
            case "getTotalNumberOfPlayerAnswerForPosition":
                int count = PAR.getAllPlayerAnswersInTeamByPosition(team_id,arg).size();
                resp = new ResponseError(count,200);
                break;
            default:
                resp = new ResponseError("Invalid request",500);
        }
        return resp;
    }


    private boolean doesTeamExist(Long id){
        return TR.findById(id).isPresent();
    }
    private boolean doesQuizQuestionExists(Long id){
        return QQR.findById(id).isPresent();
    }
    private boolean doesPlayerExist(String player_id){
        return PR.findById(player_id).isPresent();
    }
    private boolean doesPositionExist(String pos_id){
        return PosR.findById(pos_id).isPresent();
    }

    private Map<String,Boolean> PositionOrPlayer(String arg, boolean isPos){
        Map<String,Boolean> ret = new HashMap<>();
        if (isPos){
            ret.put("exists",doesPositionExist(arg));
        }else{
            ret.put("exists",doesPlayerExist(arg));
        }
        return  ret;
    }

    private JSONObject jsonify(Collection<PlayerAnswer> answers){
        JSONObject answerObject = new JSONObject();
        JSONArray answerArray = new JSONArray();
        for (PlayerAnswer playerAnswer : answers){
            answerArray.add(playerAnswer.toJSONObj());
        }

        answerObject.put("answers",answerArray);
        return answerObject;

    }
}
