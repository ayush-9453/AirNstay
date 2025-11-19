package com.cognizant.demo.entity;
 
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonProperty;
 
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
 
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Packages {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
   
    @NotNull(message = "Title cannot be null")
    private String title;
   
    @NotNull(message = "Location cannot be null")
    private String location;
   
    @NotNull(message = "Price cannot be null")
    private Integer price;
   
    @NotNull(message = "Duration cannot be null")
    private String duration;
 
    @NotNull(message = "images cannot be null")
    @Column(length = 1024)
    private String imageUrl;
 
    @NotNull(message = "Inclusions cannot be null")
    private String inclusions;
   
    @ElementCollection
    private List<String> features;
 
    @NotNull(message = "AgentID cannot be null")
    private String agentId;
 
}
 
 
