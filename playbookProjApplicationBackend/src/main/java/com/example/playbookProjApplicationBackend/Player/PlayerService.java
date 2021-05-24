package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Team.Team;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

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

    public String getAllPlayersInTeam(Long teamId){
        return new ResponseError(jsonify(PR.getPlayersByTeamId(teamId)),HttpStatus.OK.value()).toJson();
    }
    public String getAllPlayersInPosition(Long teamId, String posId){
        return new ResponseError(jsonify(PR.getPlayersByTeamPosition(teamId, posId)),HttpStatus.OK.value()).toJson();
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
    public String addNewPlayer(Map<String,Object> player){
        ResponseError resp = null;
        //check if all required fields are present
        if(!player.containsKey("student_number") || !player.containsKey("email") || !player.containsKey("first_name") || !player.containsKey("last_name")  || !player.containsKey("team_id")){
            return new ResponseError("invalid request, missing fields", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        //check if team with id exists if it does retrieve the team
        Integer id = (Integer) player.get("team_id");
        long team_id = id;
        if(!TR.findById(team_id).isPresent()){
            return new ResponseError("team with id " + team_id + " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        //check if player with the same student number does not exists
        if(doesPlayerExist((String) player.get("student_number"))){
            return new ResponseError("player with id " + player.get("student_number") + " does not exist", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            Team team = TR.getOne(team_id);
            Player player1 = new Player((String) player.get("student_number"),(String) player.get("first_name"),(String) player.get("last_name"),
            (String) player.get("email"), player.get("jersey") == null ? null : (String) player.get("jersey"),team);
            PR.save(player1);
            resp = new ResponseError("Success",HttpStatus.OK.value());
        }catch(Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.ordinal());
        }finally {
            return resp.toJson();
        }
    }
    @Transactional
    public String updatePlayer(String player_id, Map<String, Object> updates){
        ResponseError resp =null;
        //check if player exists
        if(!doesPlayerExist(player_id)){
            return new ResponseError("player with id " + player_id+ " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        //get player object
        Player player = PR.getOne(player_id);
        try{
            player.setFirstName(updates.get("first_name") == null ? player.getFirstName() : (String) updates.get("first_name"));
            player.setLastName(updates.get("last_name") == null ? player.getLastName() : (String) updates.get("last_name"));
            player.setEmail(updates.get("email") == null ? player.getEmail() : PR.getPlayerByEmail(player.getTeam().getId(), (String) updates.get("email") ) != null ? player.getEmail() : (String) updates.get("email"));
            player.setJerseyNumber(updates.get("jersey") == null ? player.getJerseyNumber() : (String) updates.get("jersey"));
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
