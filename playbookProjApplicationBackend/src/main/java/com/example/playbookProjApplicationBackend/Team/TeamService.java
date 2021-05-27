package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Quiz.Quiz;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class TeamService {

    private TeamRepository TR;

    @Autowired
    public TeamService(TeamRepository TR) {
        this.TR = TR;
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
    //delete Team
    //update Team

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
