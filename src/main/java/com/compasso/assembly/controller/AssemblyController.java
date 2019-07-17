package com.compasso.assembly.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

	@Autowired
	private AssemblyService service;

	public AssemblyController() {
		super();
	}
	
	public AssemblyController(AssemblyService service) {
		this.service = service;
	}

	@GetMapping(value = "/active")
	public Resources<Resource<Assembly>> getOnlyEnable() {
		List<Resource<Assembly>> assemblies = service.findAll().stream()
				.map(assembly -> new Resource<>(assembly,
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assembly.getId())).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assemblies")))
				.collect(Collectors.toList());

		return new Resources<>(assemblies, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel());
	}

	@GetMapping(value = "/desactive")
	public Resources<Resource<Assembly>> getOnlyDisable() {
		List<Resource<Assembly>> assemblies = service.findAll().stream()
				.map(assembly -> new Resource<>(assembly,
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assembly.getId())).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assemblies")))
				.collect(Collectors.toList());

		return new Resources<>(assemblies, ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel());
	}

	@GetMapping
	public ResponseEntity<Resources<Resource<Assembly>>> getAll() {
		List<Resource<Assembly>> assemblies = service.findAll().stream()
				.map(assembly -> new Resource<>(assembly,
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assembly.getId())).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assemblies")))
				.collect(Collectors.toList());

		return new ResponseEntity<>(
				new Resources<>(assemblies, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel()), 
				HttpStatus.OK);
		
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Resource<Assembly>> getAssemblyById(@PathVariable("id") String id) {

		Assembly assembly = service.findById(id).orElseThrow(() -> new RecordNotFoundException(id));

		return new ResponseEntity<>(
				new Resource<>(assembly, 
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(id)).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly")),
				HttpStatus.OK); 
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Resource<Assembly>> modifyAssemblyById(@PathVariable("id") String id, @Valid @RequestBody Assembly assembly) {
		Assembly newAssembly = new Assembly();
		try {
			newAssembly = service.save(assembly);
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}

		return new ResponseEntity<>( 
				new Resource<>(assembly,
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(newAssembly.getId())).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly")),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Resource<Assembly>> createAssembly(@Valid @RequestBody Assembly assembly) {
		Assembly newAssembly = new Assembly();
		try {
			newAssembly = service.save(assembly);
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}

		return new ResponseEntity<>(new Resource<>(assembly,
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(newAssembly.getId())).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly")),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteAssembly(@PathVariable String id) {
		try {
			Assembly assemblyToDelete = service.findById(id).get();
			service.delete(assemblyToDelete);
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}
	}

}
