package com.compasso.assembly.model;

import java.util.Collection;

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
public class Issue extends AbstractObject{

	private static final long serialVersionUID = 5736121596208871477L;
	
	private String description;
	private String goal;
	private Person owner;
	private Collection<Person> votes;
	private Integer duration;

}
