package com.compasso.assembly.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
/**
 * This class represent a person who vote in a issue.
 * 
 * @author thadeu.rocha
 *
 */
public class Person extends AbstractObject{

	private static final long serialVersionUID = -680135493936684656L;
	
	private String name;
	private LocalDate birthday;
	
}