package com.compasso.assembly;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.model.Person;
import com.compasso.assembly.repository.AssemblyRepository;
import com.compasso.assembly.service.AssemblyService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssemblyApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AssemblyControllerTest {

	
	@Autowired
	private AssemblyRepository assemblyRepository;

	@Autowired
	private AssemblyService assemblyService;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@MockBean
	private RestTemplate restTemplate;
	
	private final static String anyAttribute = "any_attribute";
	
	private Assembly createSimpleAssembly() {
		return Assembly.builder().
		name(UUID.randomUUID().toString()).
		owner(createSimpleOwner()).
		
		issues(createListOfIssue()).build();
	}
	
	private Collection<Issue> createListOfIssue() {
		Issue issue = Issue.builder().
		active(Boolean.TRUE).
		description(UUID.randomUUID().toString()).
		duration(180).
		goal(UUID.randomUUID().toString()).
		owner((createSimpleOwner())).build();
		
		Issue issue2 = Issue.builder().
		active(Boolean.TRUE).
		description(UUID.randomUUID().toString()).
		duration(180).
		goal(UUID.randomUUID().toString()).
		owner((createSimpleOwner())).build();
		
		Issue issue3 = Issue.builder().
		active(Boolean.TRUE).
		description(UUID.randomUUID().toString()).
		duration(180).
		goal(UUID.randomUUID().toString()).
		owner((createSimpleOwner())).build();
		
		return Arrays.asList(issue, issue2, issue3);
	}

	private Person createSimpleOwner() {
		return Person.builder().
		birthday(LocalDate.now()).
		cpf(UUID.randomUUID().toString()).
		name(UUID.randomUUID().toString()).
		build();
	}


	@Test
	public void create() throws Exception {
		final int databaseSizeBeforeCreate = assemblyRepository.findAll().size();
		final Assembly entity = createSimpleAssembly();
		final ResponseEntity<Assembly> response = testRestTemplate.postForEntity("/assembly/", entity, Assembly.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(assemblyRepository.findAll().size()).isEqualTo(databaseSizeBeforeCreate + 1);
	}
	
	@Test
	public void createWithInvalidEntity() throws Exception {
		final int databaseSizeBeforeCreate = assemblyRepository.findAll().size();
		final Assembly entity = createSimpleAssembly();
		
		JsonNode jsonNode = new ObjectMapper().valueToTree(entity);
		
		((ObjectNode)jsonNode).put(anyAttribute, "any_value");
		
		final ResponseEntity<Assembly> response = testRestTemplate.postForEntity("/assembly/", jsonNode, Assembly.class);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(assemblyRepository.findAll().size()).isEqualTo(databaseSizeBeforeCreate);
	}
	
}
