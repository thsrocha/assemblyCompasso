package com.compasso.assembly.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AbstractObject implements Serializable{
	
	private static final long serialVersionUID = 7480127956321058193L;
	
	@Id
	private String id;
	private LocalDateTime createdAt;
	private StatusObject statusObject;

}
