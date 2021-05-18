package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerAnswerService {
    private PlayerAnswerRepository PAR;
    private PlayerRepository PR;
    private PositionRepository PosR;

    @Autowired
    public PlayerAnswerService(PlayerAnswerRepository PAR, PlayerRepository PR, PositionRepository posR) {
        this.PAR = PAR;
        this.PR = PR;
        this.PosR = posR;
    }
}
