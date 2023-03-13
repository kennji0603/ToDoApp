package com.example.demo.entity;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Task {
	
    private int id;
    private int userId;
    private int typeId;
    private TaskType taskType;
    private String title;
    private String detail;
    private LocalDateTime deadline;
    

    
}
