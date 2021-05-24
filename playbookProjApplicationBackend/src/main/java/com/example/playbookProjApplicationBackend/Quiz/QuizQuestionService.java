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
    private TeamRepository TR;
    private PlayerRepository PR;
    private PositionRepository PosR;

    @Autowired
    public QuizQuestionService(QuizQuestionRepository QR, TeamRepository TR, PlayerRepository PR,PositionRepository PosR) {
        this.QR = QR;
        this.TR = TR;
        this.PR = PR;
        this.PosR = PosR;
    }

    public String getAllQuizQuestionsInDatabase(){
        return new ResponseError(jsonify(QR.findAll()),HttpStatus.OK.value()).toJson();
    }
    public String getAllQuizQuestionsForTeam(Long team_id){
        return processResponse(team_id,null,"getAllQuizQuestionsForTeam",false);
    }
    public String getAllQuestionsForTeamRandom(Long team_id){
        return processResponse(team_id,null,"getAllQuestionsForTeamRandom",false);
    }
    public String getAllQuestionsForTeamByPosition(Long team_id, String position){
        return processResponse(team_id,position,"getAllQuestionsForTeamByPosition",false);
    }
    public String getAllQuestionsForTeamByPositionRandom(Long team_id, String position){
        return processResponse(team_id,position,"getAllQuestionsForTeamByPositionRandom",false);
    }
    public String getAllAnsweredQuestionsForTeam(Long team_id){
        return processResponse(team_id,null,"getAllAnsweredQuestionsForTeam",false);
    }
    public String getCountAllAnsweredQuestionsForTeam(Long team_id){
        return processResponse(team_id,null,"getCountAllAnsweredQuestionsForTeam",false);
    }
    public String getAllAnsweredQuestionsForTeamByPlayer(Long team_id, String player_id){
        return processResponse(team_id,player_id,"getAllAnsweredQuestionsForTeamByPlayer",true);
    }
    public String getCountAnsweredQuestionsForTeamByCategory(Long team_id, String category){
        return processResponse(team_id,category,"getCountAnsweredQuestionsForTeamByCategory",false);
    }

    @Transactional
    public String insertNewQuestion(Map<String,Object> newQuestion){
        ResponseError resp = null;
        if(!newQuestion.containsKey("question") || !newQuestion.containsKey("question_type") ||!newQuestion.containsKey("correct_answer")
        || !newQuestion.containsKey("wrong_answer1")|| !newQuestion.containsKey("image_location")|| !newQuestion.containsKey("is_active")|| !newQuestion.containsKey("team_id")) {
            return new ResponseError("invalid quiz question", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        Integer id =  (Integer) newQuestion.get("team_id");
        long team_id = id;
        if(!doesTeamExist(team_id)){
            return new ResponseError("invalid request team with id " + team_id + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        String type = (String) newQuestion.get("question_type");
        if(!doesPositionExist(type)){
            return new ResponseError("invalid request question type " + type + " does not exist",HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            Team team = TR.getOne(team_id);
            String question = (String) newQuestion.get("question");
            String correct = (String) newQuestion.get("correct_answer");
            String wrongone = (String) newQuestion.get("wrong_answer1");
            String wrongtwo= (String) newQuestion.get("wrong_answer2");
            String wrongthree = (String)  newQuestion.get("wrong_answer3");
            String img = (String) newQuestion.get("image_location");
            Boolean is_active = (Boolean) newQuestion.get("is_active");
            QuizQuestion quizquestion = new QuizQuestion(img,type,question,correct,wrongone,wrongtwo,wrongthree,is_active,team);
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
        ResponseError resp = null;
        if(!doesTeamExist(team_id) || !QR.findById(question_id).isPresent()){
            return new ResponseError("invalid request", HttpStatus.BAD_REQUEST.value()).toJson();
        }
        try{
            QuizQuestion quiz = QR.getOne(question_id);
            quiz.setQuestionText(updates.get("question") == null ? quiz.getQuestionText() : (String) updates.get("question"));
            quiz.setQuestionType(updates.get("question_type") == null ? quiz.getQuestionType() : !doesPositionExist((String) updates.get("question_type")) ? quiz.getQuestionType() : (String) updates.get("question_type")) ;
            quiz.setCorrectAnswer(updates.get("correct_answer") == null ? quiz.getCorrectAnswer() : (String) updates.get("correct_answer"));
            quiz.setIncorrectAnswerOne(updates.get("wrong_answer1") == null ? quiz.getIncorrectAnswerOne() : (String) updates.get("wrong_answer1"));
            quiz.setIncorrectAnswerTwo(updates.get("wrong_answer2") == null ? quiz.getIncorrectAnswerTwo() : (String) updates.get("wrong_answer2"));
            quiz.setIncorrectAnswerThree(updates.get("wrong_answer3") == null ? quiz.getIncorrectAnswerThree() : (String) updates.get("wrong_answer3"));
            quiz.setImageLocation(updates.get("image_location") == null ? quiz.getImageLocation() : (String) updates.get("image_location"));
            resp = new ResponseError("Success", HttpStatus.OK.value());
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
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
        if(!doesTeamExist(team_id) || !QR.findById(question_id).isPresent()){
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

/*
    public String deleteAllQuestionsForPosition(Long team_id, Long position_id){}
    */

    private String processResponse(Long team_id, String arg,String methodToCall, boolean argIsPlayer){
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
            if(!doesTeamExist(team_id)){
                resp = new ResponseError("Team with id: " +String.valueOf(team_id) + " does not exist",HttpStatus.BAD_REQUEST.value());
            }else if(!exists){
                resp = new ResponseError(errMessage,HttpStatus.BAD_REQUEST.value());
            }else{
                resp = callMethod(team_id,arg,methodToCall);
            }
        }catch (Exception e){
            resp = new ResponseError(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
        }finally {
            return resp.toJson();
        }
    }

    private ResponseError callMethod(Long team_id, String arg, String methodToCall){
        ResponseError resp;
        switch (methodToCall) {
            case "getAllQuizQuestionsForTeam":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeam(team_id)),HttpStatus.OK.value());
                break;
            case "getAllQuestionsForTeamRandom":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamRandom(team_id)),HttpStatus.OK.value());
                break;
            case "getAllQuestionsForTeamByPosition":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamByPosition(team_id,arg)),HttpStatus.OK.value());
                break;
            case "getAllQuestionsForTeamByPositionRandom":
                resp = new ResponseError(jsonify(QR.getAllQuestionsForTeamByPositionRandom(team_id,arg)),HttpStatus.OK.value());
                break;
            case "getAllAnsweredQuestionsForTeam":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForTeam(team_id)),HttpStatus.OK.value());
                break;
            case "getCountAllAnsweredQuestionsForTeam":
                int count = QR.getAllAnsweredQuestionsForTeam(team_id).size();
                resp = new ResponseError(count,HttpStatus.OK.value());
                break;
            case "getAllAnsweredQuestionsForTeamByPlayer":
                resp = new ResponseError(jsonify(QR.getAllAnsweredQuestionsForTeamByPlayer(team_id,arg)),HttpStatus.OK.value());
                break;
            case "getCountAnsweredQuestionsForTeamByCategory":
                resp = new ResponseError(QR.getCountAnsweredQuestionsForTeamByCategory(team_id,arg),HttpStatus.OK.value());
                break;
            default:
                resp = new ResponseError("Invalid request",HttpStatus.BAD_REQUEST.value());
        }
        return resp;
    }

    private boolean doesTeamExist(Long id){
        return TR.findById(id).isPresent();
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
