package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

@Service
public class PlayerService {

    private PlayerRepository PR;

    @Autowired
    public PlayerService(PlayerRepository PR) {
        this.PR = PR;
    }

    public String getAllPlayersInTeam(Long teamId){
        return new ResponseError(jsonify(PR.getPlayersByTeamId(teamId)),200).toJson();
    }

    public String getAllPlayersInPosition(Long teamId, String posId){
        return new ResponseError(jsonify(PR.getPlayersByTeamPosition(teamId, posId)),200).toJson();
    }

    public String getPlayer(String player_id){
        //check if player exist, if it does return player else return exception
        String response;
        if(!(PR.findById(player_id).isPresent())){
            response = new ResponseError("This player does not exists",404).toJson();
            return response;
        }

        Player foundPlayer = PR.getOne(player_id);
        response = new ResponseError(foundPlayer.toJSONString(),200).toJson();
        return response;
    }

    public JSONObject jsonify(Collection<Player> players){
        JSONObject p = new JSONObject();
        JSONArray playersArray = new JSONArray();
        for (Player player : players){
            playersArray.add(player.toJSONString());
        }

        p.put("players",playersArray);
        return p;

    }




}
