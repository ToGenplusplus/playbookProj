package com.example.playbookProjApplicationBackend.Coach;

import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coach")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email")
    private String email;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "coach_positions",
            joinColumns = {
                    @JoinColumn(name = "coach_id", referencedColumnName = "id",
                            nullable = false, updatable = false)},
            inverseJoinColumns = {
                    @JoinColumn(name = "position_id", referencedColumnName = "position",
                            nullable = false, updatable = false)})
    private Set<Position> positions = new HashSet<>();

    protected Coach() {
    }

    public Coach(String firstName, String lastName, String email, Team team) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.team = team;
    }

    public String toJSONString(){
        JSONObject coach= new JSONObject();
        coach.put("coach_id",id);
        coach.put("first_name",firstName);
        coach.put("last_name",lastName);
        coach.put("email",email);
        coach.put("team_id",team.getId());

        return coach.toJSONString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", team=" + team +
                '}';
    }
}
