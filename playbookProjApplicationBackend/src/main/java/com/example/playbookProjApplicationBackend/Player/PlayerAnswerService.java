package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestionRepository;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public String uploadPlayerAnswer(Long id, Map<String,Object> data){
        ResponseError resp = null;
        if(!doesTeamExist(id) || !data.containsKey("player_id")|| !data.containsKey("question_id")|| !data.containsKey("is_correct")
                || !data.containsKey("answered_time")){
            return new ResponseError("invalid request missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        String player_id = (String) data.get("player_id");
        Integer qid = (Integer) data.get("question_id");
        long question_id = qid;
        if(!doesPlayerExist(player_id)){
            return new ResponseError("invalid request,player with id " +player_id+ " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        if(!doesQuizQuestionExists(question_id)){
            return new ResponseError("invalid request,question with id " +question_id+" does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            Player player = PR.getOne(player_id);
            QuizQuestion question =QQR.getOne(question_id);
            int answered_time = (int) data.get("answered_time");
            short time = (short) answered_time;
            Boolean is_correct = (Boolean) data.get("is_correct");
            PlayerAnswer playerAnswer = new PlayerAnswer(player,question,is_correct,time);
            PAR.save(playerAnswer);
            resp = new ResponseError("Success",HttpStatus.OK.value());
        }catch(Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return  resp.toJson();
        }
    }


    private String processResponse(Long team_id, String arg,Object arg2, String methodToCall, boolean isPos){
        ResponseError resp = null;
        Map<String,Boolean> ret = PositionOrPlayer(arg,isPos);
        boolean exists = ret.get("exists");
        String errMessage = isPos ? "Position " +arg+ " does not exist" : "Player " +arg+ " does not exist";
        try{
            if(!doesTeamExist(team_id)){
                resp = new ResponseError("Team "+ String.valueOf(team_id) + " does not exist",HttpStatus.BAD_REQUEST.value());
            }else if(!exists){
                resp = new ResponseError(errMessage,HttpStatus.BAD_REQUEST.value());
            }else{
                resp = callMethod(team_id,arg,arg2,methodToCall);
            }
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }

    private ResponseError callMethod(Long team_id, String arg, Object arg2, String methodToCall){
        ResponseError resp;
        switch (methodToCall) {
            case "getAllPlayerAnswersInTeamByQuestionType":
                resp = new ResponseError(jsonify(PAR.getAllPlayerAnswersInTeamByQuestionType(team_id,arg)),HttpStatus.OK.value());
                break;
            case "getAllPlayerAnswersInTeamByPosition":
                resp = new ResponseError(jsonify(PAR.getAllPlayerAnswersInTeamByPosition(team_id,arg)),HttpStatus.OK.value());
                break;
            case "getAllPlayerAnswersInTeamByPositionByPlayer":
                String player_id = (String) arg2;
                if (doesPlayerExist(player_id))
                    resp = new ResponseError(jsonify(PAR.getAllPlayerAnswersInTeamByPositionByPlayer(team_id,arg,player_id)),HttpStatus.OK.value());
                else
                    resp = new ResponseError("Player " + player_id + " does not exist",HttpStatus.BAD_REQUEST.value());
                break;
            case "getTotalAverageAnswerSpeedForQuizCategory":
                resp = new ResponseError(PAR.getTotalAverageAnswerSpeedForQuizCategory(team_id,arg),HttpStatus.OK.value());
                break;
            case "getPlayerAverageAnswerSpeed":
                resp = new ResponseError(PAR.getPlayerAverageAnswerSpeed(team_id,arg),HttpStatus.OK.value());
                break;
            case "getCountPlayerAnswerForQuestion":
                Long question_id = (Long) arg2;
                if(doesQuizQuestionExists(question_id))
                    resp = new ResponseError(PAR.getCountPlayerAnswerForQuestion(team_id,arg,(Long) arg2),HttpStatus.OK.value());
                else
                    resp = new ResponseError("Quiz question with id: " + question_id + " does not exist",HttpStatus.BAD_REQUEST.value());
                break;
            case "getTotalNumberOfPlayerAnswerForPosition":
                int count = PAR.getAllPlayerAnswersInTeamByPosition(team_id,arg).size();
                resp = new ResponseError(count,HttpStatus.OK.value());
                break;
            default:
                resp = new ResponseError("Invalid request",HttpStatus.BAD_REQUEST.value());
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
