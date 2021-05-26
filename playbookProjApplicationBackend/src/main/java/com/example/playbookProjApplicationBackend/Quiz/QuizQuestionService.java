package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Error.ResponseError;
import com.example.playbookProjApplicationBackend.Player.PlayerRepository;
import com.example.playbookProjApplicationBackend.Position.PositionRepository;
import com.example.playbookProjApplicationBackend.Team.Team;
import com.example.playbookProjApplicationBackend.Team.TeamRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class QuizQuestionService {

    private QuizQuestionRepository QR;
    private QuizRepository qR;
    private PlayerRepository PR;
    private PositionRepository PosR;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository QR, QuizRepository qR, PlayerRepository PR,PositionRepository PosR) {
        this.QR = QR;
        this.qR = qR;
        this.PR = PR;
        this.PosR = PosR;
    }

    public String getAllQuizQuestionsInDatabase(){
        return new ResponseError(jsonify(QR.findAll()),HttpStatus.OK.value()).toJson();
    }
    public String getAllQuizQuestionsForQuiz(Long team_id,Long quiz_id){
        return processResponse(new Object[]{team_id,quiz_id,"getAllQuizQuestionsForQuiz"});
    }
    public String getAllQuestionsForQuizRandom(Long team_id,Long quiz_id){
        return processResponse(new Object[]{team_id,quiz_id,"getAllQuestionsForQuizRandom"});
    }
    public String getAllAnsweredQuestionsForQuiz(Long team_id,Long quiz_id){
        return processResponse(new Object[]{team_id,quiz_id,"getAllAnsweredQuestionsForQuiz"});
    }
    public String getCountAllAnsweredQuestionsForQuiz(Long team_id,Long quiz_id){
        return processResponse(new Object[]{team_id,quiz_id,"getCountAllAnsweredQuestionsForQuiz"});
    }
    public String getAllAnsweredQuestionsForQuizByPlayer(Long team_id,Long quiz_id, String player_id){
        return processResponse(new Object[]{team_id,quiz_id,player_id,"getAllAnsweredQuestionsForQuizByPlayer"});
    }

    @Transactional
    public String insertNewQuizQuestion(Map<String,Object> newQuestion){
        ResponseError resp = null;
        if(!newQuestion.containsKey("question") ||!newQuestion.containsKey("correct_answer")
        || !newQuestion.containsKey("wrong_answer1")|| !newQuestion.containsKey("image_location")|| !newQuestion.containsKey("is_active")|| !newQuestion.containsKey("quiz_id")) {
            return new ResponseError("invalid quiz question", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        Integer id =  (Integer) newQuestion.get("quiz_id");
        long quiz_id = id;
        if(!doesQuizExist(quiz_id)){
            return new ResponseError("invalid request team with id " + quiz_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            Quiz quiz = qR.getOne(quiz_id);
            String question = (String) newQuestion.get("question");
            String correct = (String) newQuestion.get("correct_answer");
            String wrongone = (String) newQuestion.get("wrong_answer1");
            String wrongtwo= (String) newQuestion.get("wrong_answer2");
            String wrongthree = (String)  newQuestion.get("wrong_answer3");
            String img = (String) newQuestion.get("image_location");
            Boolean is_active = (Boolean) newQuestion.get("is_active");
            QuizQuestion quizquestion = new QuizQuestion(img,question,correct,wrongone,wrongtwo,wrongthree,is_active,quiz);
            QR.save(quizquestion);
            resp = new ResponseError("Success", HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }
    @Transactional
    public String updateQuizQuestion(Long team_id, Long question_id, Map<String,Object> updates){
        if(!doesQuizExist(team_id) || !QR.findById(question_id).isPresent()){
            return new ResponseError("invalid request", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        QuizQuestion quiz = QR.getOne(question_id);
        String question;
        if(updates.containsKey("question")){
            String q = (String) updates.get("question");
            if(QR.getQuestionsForTeamByName(team_id,q) != null && !quiz.getQuestionText().equals(q)){
                System.out.println(QR.getQuestionsForTeamByName(team_id,q));
                return new ResponseError("question " + q+ " already exists", HttpStatus.BAD_REQUEST.value()).toJson();
            }
            question = q;
        }else{
            question = quiz.getQuestionText();
        }
        try{
            quiz.setQuestionText(question);
            quiz.setCorrectAnswer(updates.containsKey("correct_answer") ?  (String) updates.get("correct_answer") : quiz.getCorrectAnswer());
            quiz.setIncorrectAnswerOne(updates.containsKey("wrong_answer1") ? (String) updates.get("wrong_answer1") : quiz.getIncorrectAnswerOne());
            quiz.setIncorrectAnswerTwo(updates.containsKey("wrong_answer2") ? (String) updates.get("wrong_answer2") : quiz.getIncorrectAnswerTwo());
            quiz.setIncorrectAnswerThree(updates.containsKey("wrong_answer3") ? (String) updates.get("wrong_answer3") : quiz.getIncorrectAnswerThree());
            quiz.setImageLocation(updates.containsKey("image_location") ?  (String) updates.get("image_location") : quiz.getImageLocation());
            return new ResponseError("Success", HttpStatus.OK.value()).toJson();
        }catch (Exception e){
            return new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()).toJson();
        }
    }
    @Transactional
    public String deleteAQuizQuestion(Long question_id){
        ResponseError resp = null;
        if(!QR.findById(question_id).isPresent()){
            return new ResponseError("quiz with id " + question_id + " does not exists", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            QuizQuestion question = QR.getOne(question_id);
            QR.delete(question);
            resp = new ResponseError("Success",HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }

    @Transactional
    public String deactivateQuizQuestion(Long team_id, Long question_id){
        ResponseError resp = null;
        if(!doesQuizExist(team_id) || !QR.findById(question_id).isPresent()){
            return new ResponseError("invalid request", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            QR.deactivateQuizQuestion(team_id,question_id);
            resp = new ResponseError("Success",HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }


    /**
     *
     * @param args args can either be:
     *              [team_id,question_id,methodtocall] or [team_id,question_id,player_id,methodtocall]
     * @return a jsonString representing response error
     */
    private String processResponse(Object [] args){
        /*
        ResponseError resp = null;
        boolean exists;
        String errMessage;
        if (!(arg == null)){
            Map<String,Boolean> posorplay = PositionOrPlayer( arg,argIsPlayer);
            exists = posorplay.get("exists");
            errMessage = argIsPlayer ? "Player " +arg+ " does not exist" : "Position " +arg+ " does not exist";
        }else{
            exists = true;
            errMessage = "";
        }
        try{
            if(!doesQuizExist(quiz_id)){
                resp = new ResponseError("Quiz with id: " +String.valueOf(quiz_id) + " does not exist",HttpStatus.BAD_REQUEST.value());
            }else if(!exists){
                resp = new ResponseError(errMessage,HttpStatus.BAD_REQUEST.value());
            }else{
                resp = callMethod(quiz_id,arg,methodToCall);
            }
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
        */
         return "";
    }

    private ResponseError callMethod(Long team_id, String arg, String methodToCall){
        ResponseError resp;
        switch (methodToCall) {
            case "getAllQuizQuestionsForQuiz":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeam(team_id)),HttpStatus.OK.value());
                break;
            case "getAllQuestionsForQuizRandom":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamByPositionRandom(team_id,arg)),HttpStatus.OK.value());
                break;
            case "getAllAnsweredQuestionsForQuiz":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForTeam(team_id)),HttpStatus.OK.value());
                break;
            case "getCountAllAnsweredQuestionsForQuiz":
                int count = QR.getAllAnsweredQuestionsForTeam(team_id).size();
                resp = new ResponseError(count,HttpStatus.OK.value());
                break;
            case "getAllAnsweredQuestionsForQuizByPlayer":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForTeamByPlayer(team_id,arg)),HttpStatus.OK.value());
                break;
            default:
                resp = new ResponseError("Invalid request",HttpStatus.BAD_REQUEST.value());
        }
        return resp;
    }

    private boolean doesQuizExist(Long id){
        return qR.findById(id).isPresent();
    }

    private boolean doesPlayerExist(String player_id){
        return PR.findById(player_id).isPresent();
    }
    private boolean doesPositionExist(String pos_id){
        return PosR.findById(pos_id).isPresent();
    }

    private Map<String,Boolean> PositionOrPlayer(String arg, boolean isPlayer){
        Map<String,Boolean> ret = new HashMap<>();
        if (isPlayer){
            ret.put("exists",doesPlayerExist(arg));
        }else{
            ret.put("exists",doesPositionExist(arg));
        }
        return  ret;
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
