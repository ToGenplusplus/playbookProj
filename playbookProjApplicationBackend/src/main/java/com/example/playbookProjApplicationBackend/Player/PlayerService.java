package com.example.playbookProjApplicationBackend.Player;

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

}
