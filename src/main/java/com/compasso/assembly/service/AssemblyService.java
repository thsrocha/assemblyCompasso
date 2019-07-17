package com.compasso.assembly.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.repository.AssemblyRepository;

@Service
public class AssemblyService {

	 @Autowired
	 private MongoTemplate mongoTemplate;
	 
	 @Autowired
	 private AssemblyRepository assemblyRepository;
	 
	 public Collection<Assembly> findAll(){
		 return assemblyRepository.findAll();
	 }
	 
	 public Assembly findById(String id) {
		 return assemblyRepository.findById(id).get();
	 }
	 
	public Assembly createOrUpdate(Assembly assemblyToCreateOrUpdate) {
		if(StringUtils.isEmpty(assemblyToCreateOrUpdate.getId())) {
			return assemblyRepository.insert(assemblyToCreateOrUpdate);
		} else {
			return assemblyRepository.save(assemblyToCreateOrUpdate);
		}
	}
	
	public Boolean vote(Issue issueToVote) {
		return null;
	}
	
	public Collection<Assembly> onlyAssemblyActive(){
		return null;
	}
	
	public Collection<Assembly> onlyAssemblyDesactive(){
		return null;
	}
	
	public void delete(Assembly assembly) {
		assemblyRepository.delete(assembly);
	}
	
}