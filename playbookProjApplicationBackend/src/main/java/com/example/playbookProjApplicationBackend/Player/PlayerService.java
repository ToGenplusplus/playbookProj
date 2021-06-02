package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private PlayerRepository PR;
    private ResponseError responseError;

    @Autowired
    public PlayerService(PlayerRepository PR, ResponseError responseError) {
        this.PR = PR;
        this.responseError = responseError;
    }

    public String getPlayer(String player_id){
        //check if player exist, if it does return player else return exception
        if(!(doesPlayerExist(player_id))){
            return sendResponse("This player does not exists",HttpStatus.BAD_REQUEST.value());

        }
        Player foundPlayer = PR.getOne(player_id);
        return sendResponse(foundPlayer.toJSONObj(),HttpStatus.OK.value());
    }
    @Transactional
    public String addNewPlayerAnswer(Map<String,Object> data) {
        if(!data.containsKey("player_id")|| !data.containsKey("question_id")|| !data.containsKey("is_correct")
                || !data.containsKey("answered_time")){
            return sendResponse("invalid request missing fields", HttpStatus.BAD_REQUEST.value());
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
                return sendResponse("invalid request question does not exist", HttpStatus.BAD_REQUEST.value());
            }
            answers.add(new PlayerAnswer(player,question[0],(Boolean) data.get("is_correct"),(short) time));
            player.setAnswers(answers);
            return sendResponse("success",HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
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
                        return sendResponse("player with with email " + players.getEmail() + " already exists", HttpStatus.BAD_REQUEST.value());
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
            return sendResponse("Success", HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Transactional
    public String deletePlayer(String player_id){
        try{
            if(!(doesPlayerExist(player_id))){
                return sendResponse("This player does not exists",HttpStatus.BAD_REQUEST.value());
            }
            Player player = PR.getOne(player_id);
            PR.deletePlayer(player.getPlayerId());
            return sendResponse("Success", HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
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

    private boolean doesPlayerExist(String player_id){
        return PR.findById(player_id).isPresent();
    }


    private String sendResponse(Object Message, int errorCode){
        responseError.setMessage(Message);
        responseError.setErrorCode(errorCode);
        return responseError.toJson();
    }

}
