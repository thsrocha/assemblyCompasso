package com.compasso.assembly.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.compasso.assembly.exception.RecordNotFoundException;
import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.model.Person;
import com.compasso.assembly.repository.AssemblyRepository;

@Service
public class AssemblyService {
	 
	 @Autowired
	 private AssemblyRepository assemblyRepository;
	 
	 public Collection<Assembly> findAll(){
		 return assemblyRepository.findAll();
	 }
	 
	 public Assembly findById(String id) {
		 return assemblyRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
	 }
	 
	public Assembly createOrUpdate(Assembly assemblyToCreateOrUpdate) {
		if(StringUtils.isEmpty(assemblyToCreateOrUpdate.getId())) {
			return assemblyRepository.insert(assemblyToCreateOrUpdate);
		} else {
			return assemblyRepository.save(assemblyToCreateOrUpdate);
		}
	}
	
	public Boolean vote(String issueOwnerCpf, String description, Person personWhoWantVote) {
		Assembly assemblyToVote = assemblyRepository.findByIssuesOwnerCpfIsAndIssuesDescriptionIs(issueOwnerCpf, description);
		Issue issueToVote = 
				assemblyToVote.getIssues().stream().filter(
						issue -> 
						issue.getDescription().equalsIgnoreCase(description) && 
						issue.getOwner().getCpf().equalsIgnoreCase(issueOwnerCpf) && 
						issue.getActive()).findFirst().get(); 
		if(!issueToVote.getVotes().contains(personWhoWantVote)) {
			issueToVote.getVotes().add(personWhoWantVote);
			return Boolean.TRUE;
		}else {
			return Boolean.FALSE;
		}
	}
	
	public Collection<Assembly> onlyAssemblyActive(){
		return assemblyRepository.findByIssuesActiveIs(Boolean.TRUE);
	}
	
	public Collection<Assembly> onlyAssemblyDesactive(){
		return assemblyRepository.findByIssuesActiveIs(Boolean.FALSE);
	}
	
	public void delete(Assembly assembly) {
		assemblyRepository.delete(assembly);
	}
	
}