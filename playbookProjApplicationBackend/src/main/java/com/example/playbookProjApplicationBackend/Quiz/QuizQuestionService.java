package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class QuizQuestionService {

    private QuizQuestionRepository QR;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository QR) {
        this.QR = QR;
    }

    public String getAllQuizQuestionsInDatabase(){
        return new ResponseError(jsonify(QR.findAll()),200).toJson();
    }

    public String getAllQuizQuestionsForTeam(Long id){
        return new ResponseError(jsonify(QR.getAllQuestionsForTeam(id)),200).toJson();
    }

    private JSONObject jsonify(Collection<QuizQuestion> questions){
        JSONObject questionObject = new JSONObject();
        JSONArray questionArray = new JSONArray();
        for (QuizQuestion quest : questions){
            questionArray.add(quest.toJSONString());
        }

        questionObject.put("questions",questionArray);
        return questionObject;

    }
}
