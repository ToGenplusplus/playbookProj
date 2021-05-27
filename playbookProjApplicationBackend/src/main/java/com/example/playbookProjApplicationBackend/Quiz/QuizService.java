package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
public class QuizService {

    private QuizRepository QR;

    @Autowired
    protected QuizService(QuizRepository QR) {
        this.QR = QR;
    }
    public String getQuiz(Long quiz_id){
        try{
            return new ResponseError(QR.getOne(quiz_id).toJSONObj(),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllQuizQuestionsForQuiz(Long quiz_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            return new ResponseError(jsonify(quiz.getQuestions()),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllAnsweredQuestionsForQuiz(Long quiz_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> questions= quiz.getQuestions();
            questions.removeIf(quizQuestion -> quizQuestion.getAnswers().isEmpty());
            return new ResponseError(jsonify(questions),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    //need to figure out how to implement
    public String getAllAnsweredQuestionsForQuizByPlayer(Long quiz_id ,String player_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> questions= quiz.getQuestions();
            questions.removeIf(quizQuestion -> quizQuestion.getAnswers().isEmpty());
            return new ResponseError(jsonify(QR.getAllAnsweredQuestionsForQuizByPlayer(quiz_id,player_id)),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
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
