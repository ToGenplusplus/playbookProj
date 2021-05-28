package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Player.PlayerAnswer;
import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
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
    @Transactional
    public String updateQuiz(Long quiz_id,Map<String,Object> quizUpdate){
        //change name,description,and position
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Team team = quiz.getTeam();
            Set<Position> teamPositions = team.getPositions();
            if(quizUpdate.containsKey("name")){
                for (Quiz quiz1 : team.getQuizzes()){
                    if (quiz1.getId() != quiz.getId() && quiz1.getName().equals((String) quizUpdate.get("name"))){
                        return new ResponseError("Team already has quiz with with name " + quiz1.getName() + " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
                    }
                }
            }
            quiz.setName(quizUpdate.containsKey("name") ? (String) quizUpdate.get("name"): quiz.getName());
            quiz.setDescription(quizUpdate.containsKey("description") ? (String) quizUpdate.get("description"): quiz.getDescription());
            for (Position position : teamPositions){
                if(position.getPosition().equals((String) quizUpdate.get("position"))){
                    quiz.setPosition(position);
                    break;
                }
            }
            return new ResponseError(quiz.toJSONObj(),HttpStatus.OK.value()).toJson();
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
    @Transactional
    public String addNewQuizQuestion(Map<String,Object> newQuestion){
        if(!newQuestion.containsKey("question") ||!newQuestion.containsKey("correct_answer")
                || !newQuestion.containsKey("wrong_answer1")|| !newQuestion.containsKey("is_active")|| !newQuestion.containsKey("quiz_id")) {
            return new ResponseError("invalid quiz question", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        long quiz_id = (Integer) newQuestion.get("quiz_id");
        try{
            Quiz quiz = QR.getOne(quiz_id);
            Set<QuizQuestion> quizQuestions = quiz.getQuestions();
            String question = (String) newQuestion.get("question");
            for(QuizQuestion question1 :quizQuestions){
                if (question1.getQuestionText().equals(question)){
                    return new ResponseError("invalid request, quiz with question "+ question1.getQuestionText() + " already exists",HttpStatus.OK.value()).toJson();
                }
            }
            String correct = (String) newQuestion.get("correct_answer");
            String wrongone = (String) newQuestion.get("wrong_answer1");
            String wrongtwo= (String) newQuestion.get("wrong_answer2");
            String wrongthree = (String)  newQuestion.get("wrong_answer3");
            String img = (String) newQuestion.get("img");
            Boolean is_active = (Boolean) newQuestion.get("is_active");
            QuizQuestion quizquestion = new QuizQuestion(img,question,correct,wrongone,wrongtwo,wrongthree,is_active,quiz);
            quizQuestions.add(quizquestion);
            quiz.setQuestions(quizQuestions);
            return new ResponseError("Success", HttpStatus.OK.value()).toJson();
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
    @Transactional
    public String newQuizAttempt(Map<String,Object> attemptData){
        if(!attemptData.containsKey("player_id") || !attemptData.containsKey("quiz_id")){
            return new ResponseError("invalid request missing fields",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            long quiz_id = (Integer) attemptData.get("quiz_id");
            Quiz quiz= QR.getOne(quiz_id);
            Team team= quiz.getTeam();
            Player player = null;
            for (Player player1 : team.getPlayers()){
                if(player1.getPlayerId().equals((String) attemptData.get("player_id"))){
                    player = player1;
                }
            }
            if(player == null){
                return new ResponseError("invalid request player with id " + attemptData.get("player_id") + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
            }
            PlayerQuiz playerQuiz = new PlayerQuiz(1, LocalDate.now(),player,quiz);
            player.getQuizzesTaken().add(playerQuiz);
            player.setQuizzesTaken(player.getQuizzesTaken());
            quiz.getPlayers().add(playerQuiz);
            quiz.setPlayers(quiz.getPlayers());
            return new ResponseError(playerQuiz.toJSONObj(),HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String updateQuizAttempt(Long quiz_id, String player_id){
        try{
            Quiz quiz= QR.getOne(quiz_id);
            Team team= quiz.getTeam();
            Player player = null;
            for (Player player1 : team.getPlayers()){
                if(player1.getPlayerId().equals(player_id)){
                    player = player1;
                }
            }
            if(player == null){
                return new ResponseError("invalid request player with id " + player_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
            }
            Set<PlayerQuiz> taken = player.getQuizzesTaken();
            for(PlayerQuiz playerQuiz : taken){
                if (playerQuiz.getQuiz().getId() == quiz_id && playerQuiz.getPlayer().getPlayerId().equals(player_id)){
                    playerQuiz.setNumberOfAttempts(playerQuiz.getNumberOfAttempts() + 1);
                }
            }
            return new ResponseError("Success",HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    //error need to fix
    public String deleteQuiz(Long quiz_id){
        try{
            Quiz quiz = QR.getOne(quiz_id);
            QR.delete(quiz);
            return new ResponseError(quiz.getId(),HttpStatus.OK.value()).toJson();
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
