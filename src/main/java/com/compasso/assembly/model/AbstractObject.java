package com.compasso.assembly.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import com.compasso.assembly.enums.StatusObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
public class AbstractObject implements Serializable{
	
	private static final long serialVersionUID = 7480127956321058193L;
	
	@Id
	private String id;
	private LocalDateTime createdAt;
	private StatusObject statusObject;

}
