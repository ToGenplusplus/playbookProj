package com.example.playbookProjApplicationBackend.Coach;

import javax.persistence.*;

@Entity
@Table(name="coach_positions")
public class CoachPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column (name="position", length = 4)
    private String position;

    protected CoachPosition() {
    }

    public CoachPosition(Coach coach, String position) {
        this.coach = coach;
        this.position = position;
    }

    @Override
    public String toString() {
        return "CoachPosition{" +
                "id=" + id +
                ", coach=" + coach +
                ", position='" + position + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
