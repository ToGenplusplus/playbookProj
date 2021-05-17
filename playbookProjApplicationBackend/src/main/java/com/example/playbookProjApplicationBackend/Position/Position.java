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
    @Column(name="position", length = 3)
    private String position;

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Player> players = new HashSet<>();

    @ManyToMany(mappedBy = "positions", fetch = FetchType.LAZY)
    private Set<Coach> coaches = new HashSet<>();

    protected Position() {
    }

    public Position(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Position{" +
                "position='" + position + '\'' +
                '}';
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
