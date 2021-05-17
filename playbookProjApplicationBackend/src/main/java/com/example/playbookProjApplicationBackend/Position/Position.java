package com.example.playbookProjApplicationBackend.Position;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="positions")
public class Position {

    @Id
    @Column(name="position", length = 3)
    private String position;

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
