package com.example.playbookProjApplicationBackend.Position;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Quiz.Quiz;
import com.example.playbookProjApplicationBackend.Team.Team;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="positions")
public class Position {

    @Id
    @Column(name="position", length = 10)
    private String position;
    @Column(name="position_description", columnDefinition = "TEXT")
    private String positionDescription;
    @Column(name="is_coach_specific", nullable = false)
    private boolean isCoachSpecific;

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Player> players = new HashSet<>();

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Coach> coaches = new HashSet<>();

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Quiz> quizzes = new HashSet<>();

    protected Position() {
    }

    public Position(String position,String positionDescription, boolean isCoachSpecific) {
        this.position = position;
        this.positionDescription = positionDescription;
        this.isCoachSpecific = isCoachSpecific;
    }

    @Override
    public String toString() {
        return "Position{" +
                "position='" + position + '\'' +
                ", isCoachSpecific=" + isCoachSpecific +
                ", positionDescription=" + positionDescription +
                '}';
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isCoachSpecific() {
        return isCoachSpecific;
    }

    public void setCoachSpecific(boolean coachSpecific) {
        isCoachSpecific = coachSpecific;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(Set<Coach> coaches) {
        this.coaches = coaches;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public Set<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
}
