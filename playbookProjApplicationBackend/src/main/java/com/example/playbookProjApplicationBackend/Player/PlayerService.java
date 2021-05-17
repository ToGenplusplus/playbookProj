package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private PlayerRepository PR;

    @Autowired
    public PlayerService(PlayerRepository PR) {
        this.PR = PR;
    }

    List<Player> getAllPlayersInTeam(Long teamId){
        return PR.getPlayersByTeamId(teamId);
    }

    List<Player> getAllPlayersInPosition(Long teamId, String posId){
        return PR.getPlayersByTeamPosition(teamId, posId);
    }

    String getPlayer(String player_id){
        //check if player exist, if it does return player else return exception
        String response;
        if(!(PR.findById(player_id).isPresent())){
            response = new ResponseError("This player does not exists",404).toJson();
            return response;
        }

        Player foundPlayer = PR.getOne(player_id);
        response = new ResponseError(foundPlayer,200).toJson();
        return response;
    }



}
