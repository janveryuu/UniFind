package com.example.system1.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private String reporterContact;
    private String locationFound;
    private String category;
    private String status = "AVAILABLE";
    private String imageFilename;
    private String studentId;
    
    @Column(columnDefinition = "TEXT")
    private String aiTags;
    
    private LocalDate dateLogged = LocalDate.now();

    private Double latitude;
    private Double longitude;
    
    private String verificationQuestion;
    private String verificationAnswer;
    
    // OOP Relationship: Many Items can be reported by One User
    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private User reporter;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReporterContact() { return reporterContact; }
    public void setReporterContact(String reporterContact) { this.reporterContact = reporterContact; }
    public String getLocationFound() { return locationFound; }
    public void setLocationFound(String locationFound) { this.locationFound = locationFound; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getImageFilename() { return imageFilename; }
    public void setImageFilename(String imageFilename) { this.imageFilename = imageFilename; }
    public String getAiTags() { return aiTags; }
    public void setAiTags(String aiTags) { this.aiTags = aiTags; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public LocalDate getDateLogged() { return dateLogged; }
    public void setDateLogged(LocalDate dateLogged) { this.dateLogged = dateLogged; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public String getVerificationQuestion() { return verificationQuestion; }
    public void setVerificationQuestion(String verificationQuestion) { this.verificationQuestion = verificationQuestion; }
    public String getVerificationAnswer() { return verificationAnswer; }
    public void setVerificationAnswer(String verificationAnswer) { this.verificationAnswer = verificationAnswer; }
    public User getReporter() { return reporter; }
    public void setReporter(User reporter) { this.reporter = reporter; }
}