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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.assembly.enums.StatusObject;
import com.compasso.assembly.exception.BadRequestException;
import com.compasso.assembly.exception.InternalServerErrorException;
import com.compasso.assembly.exception.RecordNotFoundException;
import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.model.Person;
import com.compasso.assembly.service.AssemblyService;

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

	@GetMapping(value = "/status/{status}")
	public ResponseEntity<Resources<Resource<Assembly>>> getByStatus(@PathVariable("status") @Valid StatusObject status) {
		List<Resource<Assembly>> assemblies = service.getAllAssemblyByStatus(status).stream()
				.map(assembly -> new Resource<>(assembly,
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assembly.getId())).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assemblies")))
				.collect(Collectors.toList());

		return new ResponseEntity<>(
				new Resources<>(assemblies, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel()), 
				HttpStatus.OK);
	}

	@GetMapping(path = "/")
	public ResponseEntity<Resources<Resource<Assembly>>> getAll() {
		
		
		List<Resource<Assembly>> assemblies = service.findAll().stream()
				.map(assembly -> new Resource<Assembly>(assembly,
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assembly.getId())).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assemblies")))
				.collect(Collectors.toList());

		return new ResponseEntity<>(
				new Resources<>(assemblies, ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withSelfRel()), 
				HttpStatus.OK);
		
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Resource<Assembly>> getAssemblyById(@PathVariable("id") @Valid String id) {

		if(StringUtils.isEmpty(StringUtils.trimAllWhitespace(id))) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST.toString());
		}
		
		Assembly assembly = service.findById(id);
		if(StringUtils.isEmpty(assembly.getId())) {
			throw new RecordNotFoundException(HttpStatus.NOT_FOUND.toString());
		}

		return new ResponseEntity<>(
				new Resource<>(assembly, 
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(id)).withSelfRel(),
						ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly")),
				HttpStatus.OK); 
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Resource<Assembly>> update(@PathVariable("id") String id, @Valid @RequestBody Assembly assemblyToUpdate) {
		
		if(StringUtils.isEmpty(id)) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST.toString());
		}
		
		Assembly assembly = service.findById(id);
		if(StringUtils.isEmpty(assembly.getId())) {
			throw new RecordNotFoundException(HttpStatus.NOT_FOUND.toString());
		}
		
		assemblyToUpdate.setId(assembly.getId());
		try {
			service.createOrUpdate(assemblyToUpdate);
		}catch (Exception e) {
			throw new InternalServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR.toString());
		}
		
		return new ResponseEntity<>( 
				new Resource<>(assemblyToUpdate,
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(assemblyToUpdate.getId())).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly")),
				HttpStatus.OK);
	}

	@PostMapping(value = "/")
	public ResponseEntity<Resource<Assembly>> createAssembly(@Valid @RequestBody Assembly assembly) {
		Assembly newAssembly = new Assembly();
		try {
			newAssembly = service.createOrUpdate(assembly);
		} catch (Exception e) {
			throw new InternalServerErrorException(e.getMessage());
		}

		return new ResponseEntity<>(new Resource<>(assembly,
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAssemblyById(newAssembly.getId())).withSelfRel(),
				ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(AssemblyController.class).getAll()).withRel("assembly")),
				HttpStatus.CREATED);
	}
	
	@PostMapping(value = "/vote")
	public ResponseEntity<Resource<Boolean>> vote(@RequestBody Assembly assembly, @RequestBody Person personToVote) {
		Issue issue = (Issue) assembly.getIssues().toArray()[0];
		if(StringUtils.isEmpty(issue)) {
			//TODO - criar msg 
			throw new InternalServerErrorException("pensar");
		}
		if(StringUtils.isEmpty(issue.getOwner().getCpf()) || StringUtils.isEmpty(issue.getDescription()) || StringUtils.isEmpty(personToVote)) {
			//TODO - criar msg 
			throw new InternalServerErrorException("pensar");
		}
			
		Boolean votedOrNot = service.vote(issue.getOwner().getCpf(), issue.getDescription(), personToVote);

		return new ResponseEntity<>(new Resource<>(votedOrNot),	HttpStatus.OK);
		
	}

	@DeleteMapping(value = "/{id}")
	public void deleteAssembly(@PathVariable String id) {
		if(StringUtils.isEmpty(id)) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST.toString());
		}
		
		Assembly assembly = service.findById(id);
		if(StringUtils.isEmpty(assembly.getId())) {
			throw new RecordNotFoundException(HttpStatus.NOT_FOUND.toString());
		}
		
		service.delete(assembly);
	}

}
