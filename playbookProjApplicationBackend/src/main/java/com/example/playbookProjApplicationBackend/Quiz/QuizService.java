package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Player.PlayerAnswer;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
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
    public String getAllAnsweredQuestionsForQuizByPlayer(Long quiz_id ,String player_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> questions= quiz.getQuestions();
            Set<QuizQuestion> answeredQuestions = new HashSet<>();
            questions.removeIf(quizQuestion -> quizQuestion.getAnswers().isEmpty());
            for(QuizQuestion question : questions){
                question.getAnswers().forEach(playerAnswer -> {if(playerAnswer.getPlayer().getPlayerId().equals(player_id)){
                    answeredQuestions.add(question);
                }
                });
            }
            return new ResponseError(jsonify(answeredQuestions),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }

    public String getAllPlayerAnswersForQuiz(Long quiz_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> questions= quiz.getQuestions();
            questions.removeIf(quizQuestion -> quizQuestion.getAnswers().isEmpty());
            Set<PlayerAnswer> answers = new HashSet<>();
            for(QuizQuestion question : questions){
                answers.addAll(question.getAnswers());
            }
            return new ResponseError(jsonifyPlayerAnswer(answers),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllPlayerAnswersForQuizQuestion(Long quiz_id, Long question_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> questions= quiz.getQuestions();
            Set<PlayerAnswer> answers = new HashSet<>();
            for(QuizQuestion question : questions){
                if(question.getId() == question_id){
                    answers = question.getAnswers();
                }
            }
            return new ResponseError(jsonifyPlayerAnswer(answers),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String getAllPlayerAnswersForQuizForPlayer(Long quiz_id,String player_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> questions= quiz.getQuestions();
            Set<PlayerAnswer> answers = new HashSet<>();
            for(QuizQuestion question : questions){
                question.getAnswers().forEach(playerAnswer -> {
                    if (playerAnswer.getPlayer().getPlayerId().equals(player_id)) {

                        answers.add(playerAnswer);
                    }});

            }
            return new ResponseError(jsonifyPlayerAnswer(answers),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    public String countQuizPlayerAttempts(Long quiz_id,String player_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<PlayerQuiz> playerQuizs =  quiz.getPlayers();
            final int[] attempts = {0};
            playerQuizs.forEach(playerQuiz -> {if (playerQuiz.getQuiz().getId() == quiz_id && playerQuiz.getPlayer().getPlayerId().equals(player_id)){
                attempts[0] = playerQuiz.getNumberOfAttempts();
            }
            });
            return new ResponseError(attempts[0],HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }

    private JSONObject jsonifyPlayerAnswer(Collection<PlayerAnswer> answers){
        JSONObject answerObject = new JSONObject();
        JSONArray answerArray = new JSONArray();
        for (PlayerAnswer playerAnswer : answers){
            answerArray.add(playerAnswer.toJSONObj());
        }

        answerObject.put("answers",answerArray);
        return answerObject;

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
