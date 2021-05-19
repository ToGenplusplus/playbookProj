package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "player_answers")
public class PlayerAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @Column(name = "is_correct", nullable = false)
    private boolean correctAnswerSelected;

    @Column(name = "answered_time",nullable = false)
    private short timeToAnswerQuestion;

    protected PlayerAnswer() {
    }

    public PlayerAnswer(Player player, QuizQuestion question, boolean correctAnswerSelected, short timeToAnswerQuestion) {
        this.player = player;
        this.question = question;
        this.correctAnswerSelected = correctAnswerSelected;
        this.timeToAnswerQuestion = timeToAnswerQuestion;
    }

    @Override
    public String toString() {
        return "PlayerAnswer{" +
                "id=" + id +
                ", player=" + player +
                ", question=" + question +
                ", correctAnswerSelected=" + correctAnswerSelected +
                ", timeToAnswerQuestion=" + timeToAnswerQuestion +
                '}';
    }

    public JSONObject toJSONObj(){
        JSONObject playerAnswer= new JSONObject();
        playerAnswer.put("id",id);
        playerAnswer.put("player_id",player.getStudentNumber());
        playerAnswer.put("question_id",question.getId());
        playerAnswer.put("is_correct",correctAnswerSelected);
        playerAnswer.put("question_answered_time",timeToAnswerQuestion);

        return playerAnswer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public QuizQuestion getQuestion() {
        return question;
    }

    public void setQuestion(QuizQuestion question) {
        this.question = question;
    }

    public boolean isCorrectAnswerSelected() {
        return correctAnswerSelected;
    }

    public void setCorrectAnswerSelected(boolean correctAnswerSelected) {
        this.correctAnswerSelected = correctAnswerSelected;
    }

    public short getTimeToAnswerQuestion() {
        return timeToAnswerQuestion;
    }

    public void setTimeToAnswerQuestion(short timeToAnswerQuestion) {
        this.timeToAnswerQuestion = timeToAnswerQuestion;
    }
}
