package com.compasso.assembly.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.assembly.exception.InternalServerErrorException;
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

	
	 @RequestMapping(value = "", method = RequestMethod.GET)
	 public Resources<Resource<Assembly>> getOnlyEnable() { 
		 List<Resource<Assembly>> assemblies = repository.findAll().stream()
					.map(assembly -> new Resource<>(assembly,
							ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class)
									.getAssemblyById(assembly.getId())).withSelfRel(),
							ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll())
									.withRel("assemblies")))
					.collect(Collectors.toList());

			return new Resources<>(assemblies, ControllerLinkBuilder
					.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel());
	 }
	
	 @RequestMapping(value = "", method = RequestMethod.GET) 
	 public Resources<Resource<Assembly>> getOnlyDisable() { 
		 List<Resource<Assembly>> assemblies = repository.findAll().stream()
					.map(assembly -> new Resource<>(assembly,
							ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class)
									.getAssemblyById(assembly.getId())).withSelfRel(),
							ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll())
									.withRel("assemblies")))
					.collect(Collectors.toList());

			return new Resources<>(assemblies, ControllerLinkBuilder
					.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel());
	 }
	 

	@RequestMapping(value = "", method = RequestMethod.GET)
	public Resources<Resource<Assembly>> getAll() {
		List<Resource<Assembly>> assemblies = repository.findAll().stream()
				.map(assembly -> new Resource<>(assembly,
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class)
								.getAssemblyById(assembly.getId())).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll())
								.withRel("assemblies")))
				.collect(Collectors.toList());

		return new Resources<>(assemblies, ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Resource<Assembly> getAssemblyById(@PathVariable("id") String id) {

		Assembly assembly = repository.findById(id).orElseThrow(() -> new RecordNotFoundException(id));

		return new Resource<>(assembly, 
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(id)).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly"));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public Resource<Assembly> modifyAssemblyById(@PathVariable("id") String id, @Valid @RequestBody Assembly assembly) {
		Assembly newAssembly = new Assembly();
		try {
			newAssembly = repository.save(assembly);
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
		
		return new Resource<>(assembly, 
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(newAssembly.getId())).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly"));
	}

	
	 @RequestMapping(value = "/", method = RequestMethod.POST) 
	 public Resource<Assembly> createAssembly(@Valid @RequestBody Assembly assembly) {
		Assembly newAssembly = new Assembly();
		try {
			newAssembly = repository.save(assembly);
		}catch(Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
		
		return new Resource<>(assembly, 
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(newAssembly.getId())).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly"));
	 }
	 
	 @RequestMapping(value = "/{id}", method = RequestMethod.DELETE) 
	 public void deleteAssembly(@PathVariable String id) {
		 try {
			 Assembly assemblyToDelete = repository.findById(id).get();
			 repository.delete(assemblyToDelete); 
		 }catch(Exception e) {
			 throw new InternalServerErrorException(e.getMessage());
		 }
	 }
	
}
