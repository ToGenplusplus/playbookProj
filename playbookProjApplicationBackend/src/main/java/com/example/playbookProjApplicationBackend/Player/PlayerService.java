package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import com.example.playbookProjApplicationBackend.Team.Team;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private PlayerRepository PR;
    private TeamRepository TR;
    private PositionRepository PosR;

    @Autowired
    public PlayerService(PlayerRepository PR, TeamRepository TR, PositionRepository PosR) {
        this.PR = PR;
        this.TR = TR;
        this.PosR = PosR;
    }

    public String getPlayer(String player_id){
        //check if player exist, if it does return player else return exception
        String response;
        if(!(doesPlayerExist(player_id))){
            response = new ResponseError("This player does not exists",HttpStatus.BAD_REQUEST.value()).toJson();
            return response;
        }

        Player foundPlayer = PR.getOne(player_id);
        response = new ResponseError(foundPlayer.toJSONObj(),HttpStatus.OK.value()).toJson();
        return response;
    }
    @Transactional
    public String addNewPlayerAnswer(Map<String,Object> data) {
        if(!data.containsKey("player_id")|| !data.containsKey("question_id")|| !data.containsKey("is_correct")
                || !data.containsKey("answered_time")){
            return new ResponseError("invalid request missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            String playerId = (String) data.get("player_id");
            long questionId = (Integer) data.get("question_id");
            int time = (int) data.get("answered_time");
            Player player = PR.getOne(playerId);
            Set<PlayerAnswer> answers = player.getAnswers();
            Set<PlayerQuiz> quizzesTaken = player.getQuizzesTaken();
            QuizQuestion [] question  = {null};
            for (PlayerQuiz playerQuiz: quizzesTaken){
                playerQuiz.getQuiz().getQuestions().forEach(quizQuestion -> {if(quizQuestion.getId() == questionId){
                    question[0] = quizQuestion;
                }
                });
            }
            if(question[0] == null){
                return new ResponseError("invalid request question does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
            }
            answers.add(new PlayerAnswer(player,question[0],(Boolean) data.get("is_correct"),(short) time));
            player.setAnswers(answers);
            return new ResponseError("success",HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String updatePlayer(String player_id, Map<String, Object> updates){
        ResponseError resp =null;
        //check if player exists
        if(!doesPlayerExist(player_id)){
            return new ResponseError("player with id " + player_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        //get player object
        Player player = PR.getOne(player_id);
        String email;
        if(updates.containsKey("email")){
            String objemail = (String) updates.get("email");
            if(PR.getPlayerByEmail(player.getTeam().getId(),objemail) != null && !player.getEmail().equals(objemail)){
                return new ResponseError("player with with email " + objemail+ " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
            }
            email = objemail;
        }else{
            email = player.getEmail();
        }
        try{
            player.setFirstName(updates.containsKey("first_name")  ?  (String) updates.get("first_name") : player.getFirstName());
            player.setLastName(updates.containsKey("last_name")  ?  (String) updates.get("last_name") : player.getLastName());
            player.setEmail(email);
            player.setJerseyNumber(updates.containsKey("jersey")  ? (String) updates.get("jersey") : player.getJerseyNumber());
            if (updates.containsKey("positions")){
                updatePlayerPositions(player_id,(ArrayList<String>) updates.get("positions"));
            }
            resp = new ResponseError("Success", HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }
    @Transactional
    public String deletePlayer(String player_id){
        ResponseError resp =null;
        if(!doesPlayerExist(player_id)){
            return new ResponseError("player with id " + player_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            PR.deleteById(player_id);
            resp = new ResponseError("Success", HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }
    private void updatePlayerPositions(String player_id,ArrayList<String> positions){
        if (positions.size() == 0){
            return;
        }
        PR.removePlayerPosition(player_id);
        for (String position: positions){
            if(PosR.existsById(position)){
                PR.insertNewPlayerPosition(player_id,position);
            }
        }
    }

    private JSONObject jsonify(Collection<Player> players){
        JSONObject p = new JSONObject();
        JSONArray playersArray = new JSONArray();
        for (Player player : players){
            playersArray.add(player.toJSONObj());
        }

        p.put("players",playersArray);
        return p;

    }

    private boolean doesPlayerExist(String player_id){
        return PR.findById(player_id).isPresent();
    }




}
