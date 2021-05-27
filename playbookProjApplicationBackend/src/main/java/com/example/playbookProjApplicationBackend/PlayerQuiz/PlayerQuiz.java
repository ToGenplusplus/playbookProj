package com.example.playbookProjApplicationBackend.PlayerQuiz;

import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Quiz.Quiz;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "player_quizzes_taken")
public class PlayerQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "num_attempts", nullable = false)
    private Integer numberOfAttempts;
    @Column(name = "last_attempt_date", nullable = false)
    private LocalDate timeOfLastAttempt;
    @ManyToOne(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    @ManyToOne(cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    protected PlayerQuiz() {
    }

    public PlayerQuiz(Integer numberOfAttempts, LocalDate timeOfLastAttempt, Player player, Quiz quiz) {
        this.numberOfAttempts = numberOfAttempts;
        this.timeOfLastAttempt = timeOfLastAttempt;
        this.player = player;
        this.quiz = quiz;
    }

    @Override
    public String toString() {
        return "PlayerQuiz{" +
                "id=" + id +
                ", numberOfAttempts=" + numberOfAttempts +
                ", timeOfLastAttempt=" + timeOfLastAttempt +
                ", player=" + player +
                ", quiz=" + quiz +
                '}';
    }

    public JSONObject toJSONObj(){
        JSONObject playerQuiz= new JSONObject();
        playerQuiz.put("id",id);
        playerQuiz.put("number_attempts",numberOfAttempts);
        playerQuiz.put("time_last_attempt",timeOfLastAttempt);
        playerQuiz.put("player_id",player.getPlayerId());
        playerQuiz.put("quiz_id",this.quiz.getId());

        return playerQuiz;
    }

    public long getId() {
        return id;
    }

    public Integer getNumberOfAttempts() {
        return numberOfAttempts;
    }

    public void setNumberOfAttempts(Integer numberOfAttempts) {
        this.numberOfAttempts = numberOfAttempts;
    }

    public LocalDate getTimeOfLastAttempt() {
        return timeOfLastAttempt;
    }

    public void setTimeOfLastAttempt(LocalDate timeOfLastAttempt) {
        this.timeOfLastAttempt = timeOfLastAttempt;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }
}
