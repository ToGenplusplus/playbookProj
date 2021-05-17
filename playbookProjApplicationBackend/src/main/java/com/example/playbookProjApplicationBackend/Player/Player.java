package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import com.example.playbookProjApplicationBackend.Team.Team;

import javax.persistence.*;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;

@Entity
@Table(name="players", uniqueConstraints = {
        @UniqueConstraint(name="player_unique_email",columnNames = {"email"})
})
public class Player {

    @Id
    @Column(name = "student_number")
    private String studentNumber;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(name = "jersey", length = 2)
    private String jerseyNumber;

    //changed from fetchtype.lazy to eager
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "player_positions",
            joinColumns = {
                    @JoinColumn(name = "player_id", referencedColumnName = "student_number",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "position_id", referencedColumnName = "position",
                            nullable = false, updatable = false)})
    private Set<Position> positions = new HashSet<>();

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<PlayerAnswer> answers;

    protected Player() {
    }

    public Player(String studentNumber, String firstName, String lastName, String email, String jerseyNumber, Team team) {
        this.studentNumber = studentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.jerseyNumber = jerseyNumber;
        this.team = team;
    }

    @Override
    public String toString() {
        return "Player{" +
                "studentNumber='" + studentNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", jerseyNumber='" + jerseyNumber + '\'' +
                ", team=" + team +
                '}';
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
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
}
