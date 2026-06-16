package com.example.system1.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
public class ClaimRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private User student;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String studentId;
    private String claimToken;
    private String proofImage; // Stores the filename of the uploaded image

    @Column(columnDefinition = "TEXT")
    private String ownershipDetails;

    private Boolean isVerificationCorrect;
    private String providedAnswer;

    private String status = "PENDING";
    private LocalDateTime requestDate = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getStudent() { return student; }
    public void setStudent(User student) { this.student = student; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getClaimToken() { return claimToken; }
    public void setClaimToken(String claimToken) { this.claimToken = claimToken; }
    public String getProofImage() { return proofImage; }
    public void setProofImage(String proofImage) { this.proofImage = proofImage; }
    public String getOwnershipDetails() { return ownershipDetails; }
    public void setOwnershipDetails(String ownershipDetails) { this.ownershipDetails = ownershipDetails; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }
    public Boolean getIsVerificationCorrect() { return isVerificationCorrect; }
    public void setIsVerificationCorrect(Boolean isVerificationCorrect) { this.isVerificationCorrect = isVerificationCorrect; }
    public String getProvidedAnswer() { return providedAnswer; }
    public void setProvidedAnswer(String providedAnswer) { this.providedAnswer = providedAnswer; }
}