package com.compasso.assembly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.compasso.assembly.repository.AssemblyRepository;

@Service
public class AssemblyService {

	 @Autowired
	 private MongoTemplate mongoTemplate;
	 
	 @Autowired
	 private AssemblyRepository assemblyRepository;
	 
	
}
