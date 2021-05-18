package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.PlayerRepository;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

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
    public String getAllQuizQuestionsForTeam(Long id){
        return processResponse(id,QR.getAllQuestionsForTeam(id));
    }
    public String getAllQuestionsForTeamRandom(Long id){
        return processResponse(id,QR.getAllQuestionsForTeamRandom(id));
    }
    public String getAllQuestionsForTeamByPosition(Long id, String position){
        return processResponsePos(id,position,QR.getAllQuestionsForTeamByPosition(id,position));
    }
    public String getAllQuestionsForTeamByPositionRandom(Long id, String position){
        return processResponsePos(id,position,QR.getAllQuestionsForTeamByPositionRandom(id,position));
    }
    public String getAllAnsweredQuestionsForTeam(Long id){
        return processResponse(id,QR.getAllAnsweredQuestionsForTeam(id));
    }
    public String getCountAllAnsweredQuestionsForTeam(Long id){
        int count = QR.getAllAnsweredQuestionsForTeam(id).size();
        return processResponse(id,count);
    }
    public String getAllAnsweredQuestionsForTeamByPlayer(Long id, String player_id){
        return processResponsePlayer(id,player_id,QR.getAllAnsweredQuestionsForTeamByPlayer(id,player_id));
    }
    public String getCountAnsweredQuestionsForTeamByCategory(Long id, String category){
        return processResponse(id,category,QR.getCountAnsweredQuestionsForTeamByCategory(id,category));
    }

    private String processResponse(Long team_id, List<QuizQuestion> questions){
        ResponseError resp;
        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team does not exist",404);
        }else{
            resp = new ResponseError(jsonify(questions),200);
        }
        return resp.toJson();
    }
    private String processResponsePos(Long team_id, String position,List<QuizQuestion> questions){
        ResponseError resp;
        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team does not exist",404);
        }else if(!doesPositionExist(position)){
            resp = new ResponseError("Position does not exist",404);
        }else{
            resp = new ResponseError(jsonify(questions),200);
        }
        return resp.toJson();
    }

    private String processResponsePlayer(Long team_id, String player,List<QuizQuestion> questions){
        ResponseError resp;
        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team does not exist",404);
        }else if(!doesPlayerExist(player)){
            resp = new ResponseError("Player does not exist",404);
        }else{
            resp = new ResponseError(jsonify(questions),200);
        }
        return resp.toJson();
    }

    private String processResponse(Long team_id,String category,int number){
        ResponseError resp;
        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team does not exist",404);
        }else if(!doesPositionExist(category)){
            resp = new ResponseError("Position does not exist",404);
        }else{
            resp = new ResponseError(number,200);
        }
        return resp.toJson();
    }
    private String processResponse(Long team_id,int number){
        ResponseError resp;
        if(!doesTeamExist(team_id)){
            resp = new ResponseError("Team does not exist",404);
        }else{
            resp = new ResponseError(number,200);
        }
        return resp.toJson();
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


    private JSONObject jsonify(Collection<QuizQuestion> questions){
        JSONObject questionObject = new JSONObject();
        JSONArray questionArray = new JSONArray();
        for (QuizQuestion quest : questions){
            questionArray.add(quest.toJSONString());
        }

        questionObject.put("questions",questionArray);
        return questionObject;

    }
}
