package com.compasso.assembly.repository;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.model.Person;

@Repository
public class AssembyRepositoryCustomImpl implements AssemblyRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Boolean vote(Issue issueToVote, Person p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Assembly> getOnlyEnable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Assembly> getOnlyDisable() {
		// TODO Auto-generated method stub
		return null;
	}

}
