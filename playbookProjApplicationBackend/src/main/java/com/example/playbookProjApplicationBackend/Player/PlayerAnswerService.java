package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PlayerAnswerService {
    private PlayerAnswerRepository PAR;
    private ResponseError responseError;

    @Autowired
    public PlayerAnswerService(PlayerAnswerRepository PAR, ResponseError responseError) {
        this.PAR = PAR;
        this.responseError = responseError;
    }

    public String getPlayerAverageAnswerSpeed(Long id, String player_id){
        try{
            return sendResponse(PAR.getPlayerAverageAnswerSpeed(id,player_id),HttpStatus.OK.value());
        }catch (Exception e){
            return sendResponse(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    private String sendResponse(Object Message, int errorCode){
        responseError.setMessage(Message);
        responseError.setErrorCode(errorCode);
        return responseError.toJson();
    }





}
