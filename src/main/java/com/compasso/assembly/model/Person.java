package com.compasso.assembly.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Person extends AbstractObject{

	private static final long serialVersionUID = -680135493936684656L;
	
	private String name;
	private LocalDate birthday;
	private String cpf;
	
}