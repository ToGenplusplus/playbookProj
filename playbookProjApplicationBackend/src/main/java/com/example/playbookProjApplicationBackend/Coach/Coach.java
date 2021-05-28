package com.example.playbookProjApplicationBackend.Coach;

import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Quiz.Quiz;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coach", uniqueConstraints = {
        @UniqueConstraint(name = "unique_coach_email_team",columnNames = {"email","team_id"})
})
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
    @OneToMany(mappedBy = "coach", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Quiz> quizzes = new HashSet<>();

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

    public JSONObject toJSONObj(){
        JSONObject coach= new JSONObject();
        coach.put("coach_id",id);
        coach.put("first_name",firstName);
        coach.put("last_name",lastName);
        coach.put("email",email);
        coach.put("team_id",team.getId());
        JSONArray positionsArray = new JSONArray();
        JSONArray quizesCreated = new JSONArray();
        for(Position position : positions){
            positionsArray.add(position.getPosition());
        }
        for(Quiz quiz: quizzes){
            quizesCreated.add(quiz.getId());
        }
        coach.put("positions",positionsArray);
        coach.put("quizzes_id",quizesCreated);

        return coach;
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

    public Set<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(Set<Quiz> quizzes) {
        this.quizzes = quizzes;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void setPositions(Set<Position> positions) {
        this.positions = positions;
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
