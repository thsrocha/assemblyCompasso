package com.compasso.assembly.repository;

import java.util.Collection;

import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.model.Person;

public interface AssemblyRepositoryCustom {
	
	Boolean vote(Issue issueToVote, Person p);
	Collection<Assembly> getOnlyEnable();
	Collection<Assembly> getOnlyDisable();
}
