package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.PlayerQuiz.PlayerQuiz;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Team.Team;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@Entity
@Table(name="players", uniqueConstraints = {
        @UniqueConstraint(name="player_unique_email_for_team",columnNames = {"email","team_id"})
})
public class Player {

    @Id
    @Column(name = "player_id")
    private String playerId;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(name = "jersey", length = 2)
    private String jerseyNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "player_positions",
            joinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "player_id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "position_id", referencedColumnName = "position",
                            nullable = false, updatable = false)})
    private Set<Position> positions = new HashSet<>();

    @OneToMany(mappedBy = "player",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlayerQuiz> quizzesTaken = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<PlayerAnswer> answers;

    protected Player() {
    }

    public Player(String playerId, String firstName, String lastName, String email, String jerseyNumber, Team team) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.jerseyNumber = jerseyNumber;
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId='" + playerId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", jerseyNumber='" + jerseyNumber + '\'' +
                ", team=" + team +
                '}';
    }

    public JSONObject toJSONObj(){
        JSONObject player= new JSONObject();
        player.put("player_id",playerId);
        player.put("first_name",firstName);
        player.put("last_name",lastName);
        player.put("email",email);
        player.put("jersey_number",jerseyNumber);
        player.put("team_id",team.getId());
        JSONArray positionsArray = new JSONArray();
        JSONArray quizesArray = new JSONArray();
        for(Position position : positions){
            positionsArray.add(position.getPosition());
        }
        for(PlayerQuiz quiz: quizzesTaken){
            quizesArray.add(quiz.getQuiz().getId());
        }
        player.put("positions",positionsArray);
        player.put("quizzes_id",quizesArray);

        return player;
    }


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(String jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public Set<PlayerQuiz> getQuizzesTaken() {
        return quizzesTaken;
    }

    public void setQuizzesTaken(Set<PlayerQuiz> quizzesTaken) {
        this.quizzesTaken = quizzesTaken;
    }

    public Set<PlayerAnswer> getAnswers() {
        return answers;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
    }
}
