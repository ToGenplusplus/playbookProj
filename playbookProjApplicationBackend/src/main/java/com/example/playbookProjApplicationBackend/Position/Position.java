package com.example.playbookProjApplicationBackend.Position;

import com.example.playbookProjApplicationBackend.Coach.Coach;
import com.example.playbookProjApplicationBackend.Player.Player;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="positions")
public class Position {

    @Id
    @Column(name="position", length = 10)
    private String position;
    @Column(name="position_description")
    private String positionDescription;
    @Column(name="is_coach_specific", nullable = false)
    private boolean isCoachSpecific;
    
    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Player> players = new HashSet<>();

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Coach> coaches = new HashSet<>();

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
}
