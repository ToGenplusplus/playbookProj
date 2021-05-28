package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Position.Position;
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

    @Autowired
    public PlayerService(PlayerRepository PR, TeamRepository TR) {
        this.PR = PR;
        this.TR = TR;
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
    @Transactional
    public String updatePlayer(String player_id, Map<String, Object> updates){
        try{
        //get player object
            Player player = PR.getOne(player_id);
            Team playersTeam = player.getTeam();
            if(updates.containsKey("email")){
                for(Player players : playersTeam.getPlayers()){
                    if (!players.getPlayerId().equals(player_id) && players.getEmail().equals((String) updates.get("email"))){
                        return new ResponseError("player with with email " + players.getEmail() + " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
                    }
                }
            }
            player.setFirstName(updates.containsKey("first_name")  ?  (String) updates.get("first_name") : player.getFirstName());
            player.setLastName(updates.containsKey("last_name")  ?  (String) updates.get("last_name") : player.getLastName());
            player.setEmail(updates.containsKey("email") ? (String) updates.get("email") : player.getEmail());
            player.setJerseyNumber(updates.containsKey("jersey")  ? (String) updates.get("jersey") : player.getJerseyNumber());
            if (updates.containsKey("positions")){
                updatePlayerPositions( player,playersTeam,(ArrayList<String>) updates.get("positions"));
            }
            return new ResponseError("Success", HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return  new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
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
    private void updatePlayerPositions(Player player, Team playersTeam, ArrayList<String> positions){
        Set<Position> playersPositions = player.getPositions();
        playersPositions.clear();
        for (Position position : playersTeam.getPositions()){
            positions.forEach(pos -> {if(position.getPosition().equals(pos)){
                playersPositions.add(position);
            }
            });
        }
        player.setPositions(playersPositions);
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
