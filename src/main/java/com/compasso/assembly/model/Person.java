package com.compasso.assembly.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class Person extends AbstractObject{

	private static final long serialVersionUID = -680135493936684656L;
	
	private String name;
	private LocalDate birthday;
	private String cpf;
	
}