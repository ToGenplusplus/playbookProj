package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Position.Position;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "quiz", uniqueConstraints = {
        @UniqueConstraint(name = "unique_quizName_forTeam",columnNames = {"name", "team_id"})
})
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "date_created", nullable = false)
    private LocalDate dateCreated;
    @Column(name = "last_modified", nullable = false)
    private LocalDate lastModified;
    @Column(name = "is_Activated", nullable = false)
    private boolean isActivated;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "position_id",nullable = false)
    private Position position;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "coach_id", nullable = false)
    private Coach createdBy;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Column(name = "team_id", nullable = false)
    private Team team;
    @OneToMany(mappedBy = "quiz",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<QuizQuestion> questions;

    public Quiz(String name, String description, LocalDate dateCreated, LocalDate lastModified, boolean isActivated, Position position, Coach createdBy, Team team) {
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.lastModified = lastModified;
        this.isActivated = isActivated;
        this.position = position;
        this.createdBy = createdBy;
        this.team = team;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastModified=" + lastModified +
                ", isActivated=" + isActivated +
                ", position=" + position +
                ", createdBy=" + createdBy +
                ", team=" + team +
                '}';
    }
    public JSONObject toJSONObj(){
        JSONObject quiz= new JSONObject();
        JSONArray questions = new JSONArray();
        quiz.put("quiz_id",id);
        quiz.put("name",name);
        quiz.put("description",description);
        quiz.put("date_created",dateCreated);
        quiz.put("last_modified",lastModified);
        quiz.put("is_activated",isActivated);
        quiz.put("position",position);
        quiz.put("coach_id",createdBy.getId());
        quiz.put("team_id",team.getId());
        for (QuizQuestion question: this.questions){
            questions.add(question.toJSONObj());
        }
        quiz.put("questions",questions);
        return quiz;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDate getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDate lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Coach getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Coach createdBy) {
        this.createdBy = createdBy;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
