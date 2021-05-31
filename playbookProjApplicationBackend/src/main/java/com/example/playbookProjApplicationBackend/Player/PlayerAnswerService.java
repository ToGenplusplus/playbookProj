package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PlayerAnswerService {
    private PlayerAnswerRepository PAR;

    @Autowired
    public PlayerAnswerService(PlayerAnswerRepository PAR) {
        this.PAR = PAR;
    }

    public String getPlayerAverageAnswerSpeed(Long id, String player_id){
        try{
            return new ResponseError(PAR.getPlayerAverageAnswerSpeed(id,player_id),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }





}
