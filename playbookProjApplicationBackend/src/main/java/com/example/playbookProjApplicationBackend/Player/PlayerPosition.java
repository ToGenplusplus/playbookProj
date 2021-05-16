package com.example.playbookProjApplicationBackend.Player;

import javax.persistence.*;
@Entity
@Table(name = "player_positions")
public class PlayerPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    @Column(name = "position", length = 4)
    private String position;

    protected PlayerPosition() {
    }

    public PlayerPosition(Player player, String position) {
        this.player = player;
        this.position = position;
    }

    @Override
    public String toString() {
        return "PlayerPosition{" +
                "id=" + id +
                ", player=" + player +
                ", position='" + position + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
