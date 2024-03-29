package com.compasso.assembly.repository;

import java.util.Collection;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.compasso.assembly.enums.StatusObject;
import com.compasso.assembly.model.Assembly;

@Repository
public interface AssemblyRepository extends MongoRepository<Assembly, String>{
	
	Collection<Assembly> findByStatusObjectIsAndIssuesStatusObjectIs(StatusObject assemblyStatus, StatusObject issueObject);
	Assembly findByIssuesOwnerCpfIsAndIssuesDescriptionIs(String cpf, String description);

}
