package com.example.playbookProjApplicationBackend.Team;

import com.example.playbookProjApplicationBackend.Coach.Coach;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="team", uniqueConstraints = {
    @UniqueConstraint(name="unique_team_name",columnNames ={"name"})
})
public class Team {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "name")
    private String Name;
    @Column(nullable = false,name="link")
    private String TeamPageLink;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<Coach> coaches;

    //need to implement players OneToMany mapping

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

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", Name='" + Name + '\'' +
                ", TeamPageLink='" + TeamPageLink + '\'' +
                '}';
    }
}
