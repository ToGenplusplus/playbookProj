package com.example.playbookProjApplicationBackend.Player;

import com.example.playbookProjApplicationBackend.Team.Team;

import javax.persistence.*;

@Entity
@Table(name="players", uniqueConstraints = {
        @UniqueConstraint(name="player_unique_email",columnNames = {"email"})
})
public class Player {

    @Id
    private String studentNumber;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email")
    private String email;
    @Column(name = "jersey")
    private String jerseyNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

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
