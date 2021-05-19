package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Quiz.QuizQuestion;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="team", uniqueConstraints = {
    @UniqueConstraint(name="unique_team_name",columnNames ={"name"})
})
public class Team {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "name")
    private String Name;
    @Column(nullable = false,name="link")
    private String TeamPageLink;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Coach> coaches;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Player> players;
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<QuizQuestion> questions;


    protected Team() {
    }

    public Team(String Name, String TeamPageLink){
        this.Name = Name;
        this.TeamPageLink = TeamPageLink;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTeamPageLink() {
        return TeamPageLink;
    }

    public void setTeamPageLink(String teamPageLink) {
        TeamPageLink = teamPageLink;
    }

    public Set<Coach> getCoaches() {
        return coaches;
    }

    public void setCoaches(Set<Coach> coaches) {
        this.coaches = coaches;
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public Set<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<QuizQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", TeamPageLink='" + TeamPageLink + '\'' +
                '}';
    }

    public JSONObject toJSONObj() {
        JSONObject teamObj = new JSONObject();
        teamObj.put("id",id);
        teamObj.put("name",Name);
        teamObj.put("link",TeamPageLink);

        return  teamObj;
    }
}
