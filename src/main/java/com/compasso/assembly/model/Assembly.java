package com.compasso.assembly.model;
/**
 * This class represent an "Assembleia", with every attribute to know about the issues("pautas")
 * and another details.
 * 
 * @author thadeu.rocha
 *
 */

import java.util.Collection;

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
public class Assembly extends AbstractObject {
	
	private static final long serialVersionUID = 523637575479981278L;
	
	private String name;
	private Person owner;
	private Collection<Issue> issues;

}