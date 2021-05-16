package com.example.playbookProjApplicationBackend.Quiz;

import com.example.playbookProjApplicationBackend.Player.Player;
import com.example.playbookProjApplicationBackend.Player.PlayerAnswer;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "quiz_question")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="image_location", nullable = false, columnDefinition="TEXT")
    private String imageLocation;
    @Column(name="question_type", nullable = false, length = 4)
    private String questionType;
    @Column(name="question", nullable = false)
    private String questionText;
    @Column(name="correct_answer", nullable = false)
    private String correctAnswer;
    @Column(name="wrong_answer1", columnDefinition="TEXT", nullable = false)
    private String incorrectAnswerOne;
    @Column(name="wrong_answer2", columnDefinition="TEXT")
    private String incorrectAnswerTwo;
    @Column(name="wrong_answer3", columnDefinition="TEXT")
    private String incorrectAnswerThree;

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private Set<PlayerAnswer> answers;


    protected QuizQuestion() {
    }

    public QuizQuestion(String imageLocation, String questionType,String questionText,
                        String correctAnswer, String incorrectAnswerOne,
                        String incorrectAnswerTwo, String incorrectAnswerThree) {
        this.imageLocation = imageLocation;
        this.questionType = questionType;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswerOne = incorrectAnswerOne;
        this.incorrectAnswerTwo = incorrectAnswerTwo;
        this.incorrectAnswerThree = incorrectAnswerThree;
    }

    public QuizQuestion(String imageLocation, String questionType,String questionText,
                        String correctAnswer, String incorrectAnswerOne) {
        this.imageLocation = imageLocation;
        this.questionType = questionType;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswerOne = incorrectAnswerOne;
    }

    public QuizQuestion(String imageLocation, String questionType,String questionText,
                        String correctAnswer, String incorrectAnswerOne, String incorrectAnswerTwo) {
        this.imageLocation = imageLocation;
        this.questionType = questionType;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswerOne = incorrectAnswerOne;
        this.incorrectAnswerTwo = incorrectAnswerTwo;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "id=" + id +
                ", imageLocation='" + imageLocation + '\'' +
                ", questionType='" + questionType + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", incorrectAnswerOne='" + incorrectAnswerOne + '\'' +
                ", incorrectAnswerTwo='" + incorrectAnswerTwo + '\'' +
                ", incorrectAnswerThree='" + incorrectAnswerThree + '\'' +
                '}';
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
}
