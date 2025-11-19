package com.cognizant.demo.dto;
 
import java.util.List;
 
import com.fasterxml.jackson.annotation.JsonProperty;
 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
 
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackagesDTO {
 
 
    private int id;
    private String title;
    private String location;
    private Integer price;
    private String duration;
    private String imageUrl;
    private String inclusions;
    private List<String> features;
   
    @JsonProperty("AgentId")
    private String agentId;
}
 
