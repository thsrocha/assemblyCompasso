package com.compasso.assembly.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.compasso.assembly.repository.AssemblyRepository;

@RestController
@RequestMapping("/assembly")
public class AssemblyController {

	private AssemblyRepository respository;
	
	/**
	 * getById
	 * getAll
	 * onlyEnable
	 */
	
}
