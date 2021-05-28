package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Service
public class QuizQuestionService {

    private QuizQuestionRepository QR;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository QR) {
        this.QR = QR;
    }

    public String getAllQuizQuestionsInDatabase(){
        return new ResponseError(jsonify(QR.findAll()),HttpStatus.OK.value()).toJson();
    }
    @Transactional
    public String updateQuizQuestion(Long question_id, Map<String,Object> updates){
        try{
            QuizQuestion quizQuestion = QR.getOne(question_id);
            Quiz quiz = quizQuestion.getQuiz();
            for(QuizQuestion question: quiz.getQuestions()){
                if (question.getId() != quizQuestion.getId() && question.getQuestionText().equals((String) updates.get("question"))){
                    return new ResponseError("Quiz question already exists", HttpStatus.BAD_REQUEST.value()).toJson();
                }
            }
            quizQuestion.setQuestionText(updates.containsKey("question") ? (String) updates.get("question") : quizQuestion.getQuestionText() );
            quizQuestion.setCorrectAnswer(updates.containsKey("correct_answer") ?  (String) updates.get("correct_answer") : quizQuestion.getCorrectAnswer());
            quizQuestion.setIncorrectAnswerOne(updates.containsKey("wrong_answer1") ? (String) updates.get("wrong_answer1") : quizQuestion.getIncorrectAnswerOne());
            quizQuestion.setIncorrectAnswerTwo(updates.containsKey("wrong_answer2") ? (String) updates.get("wrong_answer2") : quizQuestion.getIncorrectAnswerTwo());
            quizQuestion.setIncorrectAnswerThree(updates.containsKey("wrong_answer3") ? (String) updates.get("wrong_answer3") : quizQuestion.getIncorrectAnswerThree());
            quizQuestion.setImageLocation(updates.containsKey("img") ?  (String) updates.get("img") : quizQuestion.getImageLocation());
            quiz.setLastModified(LocalDate.now());
            return new ResponseError("Success", HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String deleteAQuizQuestion(Long question_id){
        try{
            QuizQuestion question = QR.getOne(question_id);
            QR.delete(question);
            return new ResponseError("Success",HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }

    private JSONObject jsonify(Collection<QuizQuestion> questions){
        JSONObject questionObject = new JSONObject();
        JSONArray questionArray = new JSONArray();
        for (QuizQuestion quest : questions){
            questionArray.add(quest.toJSONObj());
        }
        questionObject.put("questions",questionArray);
        return questionObject;
    }
}
