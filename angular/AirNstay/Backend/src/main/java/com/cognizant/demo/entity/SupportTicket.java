package com.cognizant.demo.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupportTicket {

    @Id
    private String ticketId;

    private String userID;
    private String name;
    private String detailedIssue;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String status = "Pending";
    private String agent = "";
    private boolean agentAssigned = false;
    private boolean completed = false;
}