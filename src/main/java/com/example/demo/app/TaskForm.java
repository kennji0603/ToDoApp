package com.example.demo.app;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class TaskForm {
	    
	
	    @Digits(integer = 1, fraction = 0)
	    private Integer typeId;

	    @NotNull (message = "タイトルを入力してください。")
	    @Size(min = 1, max = 20, message = "20文字以内で入力してください。")
	    private String title;

	    @NotNull (message = "内容を入力してください。")
	    private String detail;

	    @NotNull (message = "期限を設定してください。")
	    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	    @Future (message = "期限が過去に設定されています。")
	    private LocalDateTime deadline;

	    public Boolean isnewTask;

		

		
		
}
