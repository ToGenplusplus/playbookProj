package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Player.PlayerAnswer;
import com.example.playbookProjApplicationBackend.Team.Team;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "question")
@Table(name = "quiz_questions", uniqueConstraints = {
        @UniqueConstraint(name = "unique_question_name_team", columnNames = {"question","team_id"})
})
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="image_location", nullable = false, columnDefinition="TEXT")
    private String imageLocation;
    @Column(name="question_type", nullable = false, length = 4)
    private String questionType;
    @Column(name="question", nullable = false, columnDefinition="TEXT")
    private String questionText;
    @Column(name="correct_answer", nullable = false, columnDefinition="TEXT")
    private String correctAnswer;
    @Column(name="wrong_answer1", columnDefinition="TEXT", nullable = false)
    private String incorrectAnswerOne;
    @Column(name="wrong_answer2", columnDefinition="TEXT")
    private String incorrectAnswerTwo;
    @Column(name="wrong_answer3", columnDefinition="TEXT")
    private String incorrectAnswerThree;
    @Column(name="is_active", nullable = false)
    private boolean isActive;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<PlayerAnswer> answers;


    protected QuizQuestion() {
    }

    public QuizQuestion(String imageLocation, String questionType,String questionText,
                        String correctAnswer, String incorrectAnswerOne,
                        String incorrectAnswerTwo, String incorrectAnswerThree,boolean isActive, Team team) {
        this.imageLocation = imageLocation;
        this.questionType = questionType;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswerOne = incorrectAnswerOne;
        this.incorrectAnswerTwo = incorrectAnswerTwo;
        this.incorrectAnswerThree = incorrectAnswerThree;
        this.team = team;
        this.isActive = isActive;
    }

    public QuizQuestion(String imageLocation, String questionType,String questionText,
                        String correctAnswer, String incorrectAnswerOne,boolean isActive, Team team) {
        this.imageLocation = imageLocation;
        this.questionType = questionType;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswerOne = incorrectAnswerOne;
        this.team = team;
        this.isActive = isActive;
    }

    public QuizQuestion(String imageLocation, String questionType,String questionText,
                        String correctAnswer, String incorrectAnswerOne, String incorrectAnswerTwo,boolean isActive, Team team) {
        this.imageLocation = imageLocation;
        this.questionType = questionType;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswerOne = incorrectAnswerOne;
        this.incorrectAnswerTwo = incorrectAnswerTwo;
        this.team = team;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "id=" + id +
                ", imageLocation='" + imageLocation + '\'' +
                ", questionType='" + questionType + '\'' +
                ", questionText='" + questionText + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswerOne='" + incorrectAnswerOne + '\'' +
                ", incorrectAnswerTwo='" + incorrectAnswerTwo + '\'' +
                ", incorrectAnswerThree='" + incorrectAnswerThree + '\'' +
                ", team=" + team +
                ", answers=" + answers +
                '}';
    }

    public JSONObject toJSONObj(){
        JSONObject question= new JSONObject();
        question.put("question_id",id);
        question.put("image_location",imageLocation);
        question.put("question_type",questionType);
        question.put("correctAnswer",correctAnswer);
        question.put("incorrect_answer_1",incorrectAnswerOne);
        question.put("incorrect_answer_2",incorrectAnswerTwo);
        question.put("incorrect_answer_3",incorrectAnswerThree);
        question.put("team_id",team.getId());
        question.put("is_active",isActive);

        return question;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getIncorrectAnswerOne() {
        return incorrectAnswerOne;
    }

    public void setIncorrectAnswerOne(String incorrectAnswerOne) {
        this.incorrectAnswerOne = incorrectAnswerOne;
    }

    public String getIncorrectAnswerTwo() {
        return incorrectAnswerTwo;
    }

    public void setIncorrectAnswerTwo(String incorrectAnswerTwo) {
        this.incorrectAnswerTwo = incorrectAnswerTwo;
    }

    public String getIncorrectAnswerThree() {
        return incorrectAnswerThree;
    }

    public void setIncorrectAnswerThree(String incorrectAnswerThree) {
        this.incorrectAnswerThree = incorrectAnswerThree;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
