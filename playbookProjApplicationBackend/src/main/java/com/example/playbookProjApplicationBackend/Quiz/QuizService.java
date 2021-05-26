package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

@Service
public class QuizService {

    private QuizRepository QR;

    @Autowired
    protected QuizService(QuizRepository QR) {
        this.QR = QR;
    }

    //implement retreival of a specific quiz
    public String getAllQuizQuestionsForQuiz( @PathVariable("quiz_id")Long quiz_id){
        return processResponse(new Object[]{quiz_id,"getAllQuizQuestionsForQuiz"});
    }
    public String getAllQuestionsForQuizRandom( @PathVariable("quiz_id")Long quiz_id){
        return processResponse(new Object[]{quiz_id,"getAllQuestionsForQuizRandom"});
    }
    public String getAllAnsweredQuestionsForQuiz(@PathVariable("quiz_id")Long quiz_id){
        return processResponse(new Object[]{quiz_id,"getAllAnsweredQuestionsForQuiz"});
    }
    public String getCountAllAnsweredQuestionsForQuiz( @PathVariable("quiz_id")Long quiz_id){
        return processResponse(new Object[]{quiz_id,"getCountAllAnsweredQuestionsForQuiz"});
    }
    public String getAllAnsweredQuestionsForQuizByPlayer(@PathVariable("quiz_id")Long quiz_id ,@PathVariable("player_id") String player_id){
        return processResponse(new Object[]{quiz_id,player_id ,"getAllAnsweredQuestionsForQuizByPlayer"});
    }

    private String processResponse(Object [] args){
        int len = args.length;
        if(len != 2 && len != 3){
            return new ResponseError("invalid number of arguments", HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
        if(!doesQuizExist((Long) args[0])){
            return new ResponseError("quiz with id " + args[0] + " does not exits",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            if(len == 2){
                return callMethod( (Long) args[0],null,(String) args[1]).toJson();
            }else{
                return callMethod((Long) args[0],(String) args[1],(String) args[2]).toJson();
            }
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    private ResponseError callMethod(Long quiz_id, String player_id, String methodToCall){
        ResponseError resp;
        switch (methodToCall) {
            case "getAllQuizQuestionsForQuiz":
                resp = new ResponseError(jsonify(QR.getAllQuizQuestionsForQuiz(quiz_id)),HttpStatus.OK.value());
                break;
            case "getAllQuestionsForQuizRandom":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForQuizRandom(quiz_id)),HttpStatus.OK.value());
                break;
            case "getAllAnsweredQuestionsForQuiz":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForQuiz(quiz_id)),HttpStatus.OK.value());
                break;
            case "getCountAllAnsweredQuestionsForQuiz":
                int count = QR.getAllAnsweredQuestionsForQuiz(quiz_id).size();
                resp = new ResponseError(count,HttpStatus.OK.value());
                break;
            case "getAllAnsweredQuestionsForQuizByPlayer":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForQuizByPlayer(quiz_id,player_id)),HttpStatus.OK.value());
                break;
            default:
                resp = new ResponseError("Invalid request",HttpStatus.BAD_REQUEST.value());
        }
        return resp;
    }

    private boolean doesQuizExist(Long id){
        return QR.findById(id).isPresent();
    }

    private JSONObject jsonify(Collection<QuizQuestion> questions){
        JSONObject quizzesObject = new JSONObject();
        JSONArray quizArray = new JSONArray();
        for (QuizQuestion quiz : questions){
            quizArray.add(quiz.toJSONObj());
        }
        quizzesObject.put("questions",quizArray);
        return quizzesObject;
    }
}
