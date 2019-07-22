package com.compasso.assembly;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.compasso.assembly.enums.StatusObject;
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
	
	private final static String emptyString = " ";
	
	private Assembly createSimpleAssembly() {
		return Assembly.builder().
		createdAt(LocalDateTime.now()).
		statusObject(StatusObject.ACTIVE).
		name(UUID.randomUUID().toString()).
		owner(createSimpleOwner()).
		
		issues(createListOfIssue()).build();
	}
	
	private Collection<Issue> createListOfIssue() {
		Issue issue = Issue.builder().
		createdAt(LocalDateTime.now()).
		statusObject(StatusObject.ACTIVE).
		votes(new ArrayList<Person>()).
		description(UUID.randomUUID().toString()).
		duration(180).
		goal(UUID.randomUUID().toString()).
		owner((createSimpleOwner())).build();
		
		Issue issue2 = Issue.builder().
		createdAt(LocalDateTime.now()).
		statusObject(StatusObject.ACTIVE).
		votes(Arrays.asList(createSimpleOwner(), createSimpleOwner())).
		description(UUID.randomUUID().toString()).
		duration(180).
		goal(UUID.randomUUID().toString()).
		owner((createSimpleOwner())).build();
		
		Issue issue3 = Issue.builder().
		createdAt(LocalDateTime.now()).
		statusObject(StatusObject.ACTIVE).
		votes(Arrays.asList(createSimpleOwner(), createSimpleOwner())).
		description(UUID.randomUUID().toString()).
		duration(180).
		goal(UUID.randomUUID().toString()).
		owner((createSimpleOwner())).build();
		
		return Arrays.asList(issue, issue2, issue3);
	}

	private Person createSimpleOwner() {
		return Person.builder().
		createdAt(LocalDateTime.now()).
		statusObject(StatusObject.ACTIVE).
		birthday(LocalDate.now()).
		cpf(UUID.randomUUID().toString()).
		name(UUID.randomUUID().toString()).
		build();
	}

	
	@Test
	public void getOne() throws Exception {
		final Assembly entity = createSimpleAssembly();
		assemblyService.createOrUpdate(entity);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.GET, null, Assembly.class, entity.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isEqualTo(entity.getId());
	}
	
	@Test
	public void getOneNotfound() throws Exception {
		final Assembly entity = createSimpleAssembly();
		assemblyService.createOrUpdate(entity);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.GET, null, Assembly.class, UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void getOneIdNull() throws Exception {
		final Assembly entity = createSimpleAssembly();
		assemblyService.createOrUpdate(entity);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.GET, null, Assembly.class, emptyString);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
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
	
	@Test
	public void update() throws Exception {
		final Assembly entity = createSimpleAssembly();
		final Person ownerUpdated = createSimpleOwner();
		assemblyService.createOrUpdate(entity);
		final int databaseSizeAfterCreate = assemblyRepository.findAll().size();
		entity.setName("Thadeu");
		entity.setOwner(ownerUpdated);
		HttpEntity<Assembly> httpEntity = new HttpEntity<Assembly>(entity);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.PUT, httpEntity, Assembly.class, entity.getId());
		Assembly assembltAfterUpdate = assemblyService.findById(entity.getId());
		assertThat(assembltAfterUpdate.getName()).isEqualTo("Thadeu");
		assertThat(ownerUpdated).isEqualTo(ownerUpdated);
		assertThat(response.getBody().getName()).isEqualTo(entity.getName());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(assemblyRepository.findAll().size()).isEqualTo(databaseSizeAfterCreate);
	}
	
	@Test
	public void updateWithInvalidEntity() throws Exception {
		final Assembly entity = createSimpleAssembly();
		assemblyService.createOrUpdate(entity);
		JsonNode jsonNode = new ObjectMapper().valueToTree(entity);
		((ObjectNode)jsonNode).put(anyAttribute, "any_value");
		HttpEntity<JsonNode> httpEntity = new HttpEntity<JsonNode>(jsonNode);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.PUT, httpEntity, Assembly.class, entity.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}
	
	
	@Test
	public void updateWithInvalidEntityId() throws Exception {
		final Assembly entity = createSimpleAssembly();
		assemblyService.createOrUpdate(entity);
		HttpEntity<Assembly> httpEntity = new HttpEntity<Assembly>(entity);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.PUT, httpEntity, Assembly.class, UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void updateWithInvalidEntityIdNull() throws Exception {
		final Assembly entity = createSimpleAssembly();
		assemblyService.createOrUpdate(entity);
		HttpEntity<Assembly> httpEntity = new HttpEntity<Assembly>(entity);
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.PUT, httpEntity, Assembly.class, emptyString);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void delete() throws Exception {
		final Assembly entity = createSimpleAssembly();
		final Assembly entity2 = createSimpleAssembly();
		assemblyRepository.saveAll(Arrays.asList(entity, entity2));
		final int databaseSizeAfterCreate = assemblyRepository.findAll().size();
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.DELETE, null, Assembly.class, entity.getId());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(assemblyRepository.findAll().size()).isEqualTo(databaseSizeAfterCreate - 1);
	}
	
	@Test
	public void deleteNotFound() throws Exception {
		final Assembly entity = createSimpleAssembly();
		final Assembly entity2 = createSimpleAssembly();
		assemblyRepository.saveAll(Arrays.asList(entity, entity2));
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.DELETE, null, Assembly.class, UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void deleteBadRequest() throws Exception {
		final Assembly entity = createSimpleAssembly();
		final Assembly entity2 = createSimpleAssembly();
		assemblyRepository.saveAll(Arrays.asList(entity, entity2));
		final ResponseEntity<Assembly> response = testRestTemplate.exchange("/assembly/{id}", HttpMethod.DELETE, null, Assembly.class, UUID.randomUUID().toString());
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
}
