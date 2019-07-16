package com.compasso.assembly.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.compasso.assembly.model.Assembly;

public interface AssemblyRepository extends MongoRepository<Assembly, String>, AssemblyRepositoryCustom{

}
