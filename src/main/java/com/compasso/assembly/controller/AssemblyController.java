package com.compasso.assembly.controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.assembly.exception.RecordNotFoundException;
import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.repository.AssemblyRepository;

@RestController
@RequestMapping("/assembly")
public class AssemblyController {

	private AssemblyRepository repository;
	
	
	
	public AssemblyController(AssemblyRepository repository) {
		this.repository = repository;
	}

	/*
	 * @RequestMapping(value = "", method = RequestMethod.GET) public
	 * Resource<Collection<Assembly>> getOnlyEnable() { return repository.findAll();
	 * }
	 * 
	 * @RequestMapping(value = "", method = RequestMethod.GET) public
	 * Resource<Assembly> getOnlyDisable() { return repository.findAll(); }
	 */
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Resources<Resource<Assembly>> getAll() {
		List<Resource<Assembly>> assemblies = repository.findAll().stream()
				    .map(assembly -> new Resource<>(assembly,
				      ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assembly.getId())).withSelfRel(),
				      ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assemblies")))
				    .collect(Collectors.toList());
		
		return new Resources<>(assemblies, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Resource<Assembly> getAssemblyById(@PathVariable("id") String id) {
	  
		Assembly assembly = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));
		
		return new Resource<>(assembly,
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(id)).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly"));
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void modifyAssemblyById(@PathVariable("id") String id, @Valid @RequestBody Assembly assembly) {
	  //pets.set_id(id);
	  repository.save(assembly);
	}
	
	/*
	 * @RequestMapping(value = "/", method = RequestMethod.POST) public
	 * Resource<Assembly> createAssembly(@Valid @RequestBody Assembly assembly) {
	 * //assembly.set_id(ObjectId.get()); //repository.save(assembly); return
	 * assembly; }
	 * 
	 * @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) public void
	 * deleteAssembly(@PathVariable String id) {
	 * repository.delete(repository.findById(id)); }
	 */
}
