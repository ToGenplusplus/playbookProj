package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Quiz.Quiz;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TeamService {

    private TeamRepository TR;

    @Autowired
    public TeamService(TeamRepository TR) {
        this.TR = TR;
    }

    public String getAllCoachesInTeam(Long team_id){
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("Team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Team team = TR.getOne(team_id);
            return new ResponseError(jsonifyCoaches(team.getCoaches()),HttpStatus.OK.value()).toJson();

        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getCoachesByPosition(Long team_id, String position_id){
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("Team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Team team = TR.getOne(team_id);
            Set<Coach> coaches = team.getCoaches();
            Set<Coach> coachesCoachingPosition = new HashSet<>();
            for(Coach coach: coaches){
                coach.getPositions().forEach(position -> {if(position.getPosition().equals(position_id)){
                    coachesCoachingPosition.add(coach);
                }
                });
            }
            return new ResponseError(jsonifyCoaches(coachesCoachingPosition),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllPlayersInTeam(Long team_id){
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("Team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Team team = TR.getOne(team_id);
            return new ResponseError(jsonifyPlayers(team.getPlayers()),HttpStatus.OK.value()).toJson();

        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllPlayersInATeamsPosition(Long team_id,String position_id){
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("Team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Team team = TR.getOne(team_id);
            Set<Player> players = team.getPlayers();
            Set<Player> playersInPosition = new HashSet<>();
            for(Player player: players){
                player.getPositions().forEach(position -> {if(position.getPosition().equals(position_id)){
                    playersInPosition.add(player);
                }
                });
            }
            return new ResponseError(jsonifyPlayers(playersInPosition),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
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
        if(!TR.findById(team_id).isPresent()){
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
        Team team = TR.getOne(team_id);
        Set<PlayerQuiz> quizzesTaken = null;
        for(Player player : team.getPlayers()){
            if(player.getPlayerId().equals(player_id)){
                quizzesTaken = player.getQuizzesTaken();
            }
        }
        if(quizzesTaken == null){
            return new ResponseError("invalid request player with id "+player_id+ " does not exists",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try {
            Set<Quiz> quizzes = new HashSet<>();
            quizzesTaken.forEach(playerQuiz -> {quizzes.add(playerQuiz.getQuiz());});
            return new ResponseError(jsonifyQuizzes(quizzes),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String addNewPlayer(Map<String,Object> newPlayer){
        if(!newPlayer.containsKey("player_id") || !newPlayer.containsKey("email") || !newPlayer.containsKey("first_name") || !newPlayer.containsKey("last_name")
                || !newPlayer.containsKey("team_id") || !newPlayer.containsKey("positions")){
            return new ResponseError("invalid request, missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            long team_id = (Integer) newPlayer.get("team_id");
            Team team = TR.getOne(team_id);
            Set<Player> players = team.getPlayers();
            for(Player player : players){
                if (player.getEmail().equals((String) newPlayer.get("email")) || player.getPlayerId().equals((String) newPlayer.get("player_id"))){
                    return new ResponseError("invalid request duplicate player found", HttpStatus.BAD_REQUEST.value()).toJson();
                }
            }
            Set<Position> teamsPositions = team .getPositions();
            Player player = new Player((String) newPlayer.get("player_id"),(String) newPlayer.get("first_name"),(String) newPlayer.get("last_name"),
                    (String) newPlayer.get("email"),(String) newPlayer.get("jersey"),team);
            players.add(player);
            team.setPlayers(players);
            for (String pos : (ArrayList<String>) newPlayer.get("positions")){
                teamsPositions.forEach(position -> {if (position.getPosition().equals(pos)){
                    player.getPositions().add(position);
                }
                });
            }
            player.setPositions(player.getPositions());
            return new ResponseError(player.toJSONObj(),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String addNewCoach(Map<String,Object> newCoach){
        if(!newCoach.containsKey("email") || !newCoach.containsKey("first_name") || !newCoach.containsKey("last_name")
                || !newCoach.containsKey("team_id") || !newCoach.containsKey("positions")){
            return new ResponseError("invalid request, missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            long team_id = (Integer) newCoach.get("team_id");
            Team team = TR.getOne(team_id);
            Set<Coach> coaches = team.getCoaches();
            for(Coach coach: coaches){
                if (coach.getEmail().equals((String) newCoach.get("email"))){
                    return new ResponseError("invalid request coach email"+ coach.getEmail() + " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
                }
            }
            Set<Position> teamsPositions = team.getPositions();
            Coach coach = new Coach((String) newCoach.get("first_name"),(String) newCoach.get("last_name"),
                    (String) newCoach.get("email"),team);
            coaches.add(coach);
            team.setCoaches(coaches);
            for (String pos : (ArrayList<String>) newCoach.get("positions")){
                teamsPositions.forEach(position -> {if (position.getPosition().equals(pos)){
                    coach.getPositions().add(position);
                }
                });
            }
            coach.setPositions(coach.getPositions());
            return new ResponseError(coach.toJSONObj(),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    //delete Team
    //update Team

    private JSONObject jsonifyCoaches(Collection<Coach> coaches){
        JSONObject coachObject = new JSONObject();
        JSONArray coachesArray = new JSONArray();
        for (Coach coach : coaches){
            coachesArray.add(coach.toJSONObj());
        }

        coachObject.put("coaches",coachesArray);
        return coachObject;

    }
    private JSONObject jsonifyPlayers(Collection<Player> players){
        JSONObject playerObj  = new JSONObject();
        JSONArray playersArray = new JSONArray();
        for (Player player: players){
            playersArray.add(player.toJSONObj());
        }

        playerObj.put("players",playersArray);
        return playerObj;
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
