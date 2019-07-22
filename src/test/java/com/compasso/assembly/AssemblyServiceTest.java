package com.compasso.assembly;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.compasso.assembly.model.Assembly;
import com.compasso.assembly.model.Issue;
import com.compasso.assembly.model.Person;
import com.compasso.assembly.repository.AssemblyRepository;
import com.compasso.assembly.service.AssemblyService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssemblyApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class AssemblyServiceTest {
	
	@Autowired
	private AssemblyRepository assemblyRepository;

	@Autowired
	private AssemblyService assemblyService;

	@Autowired
	private TestRestTemplate testRestTemplate;
	
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
	public void testCorrectVote() {
		Assembly assembly = createSimpleAssembly();
		Issue issue = assembly.getIssues().iterator().next();
		Mockito.when(assemblyRepository.findByIssuesOwnerCpfIsAndIssuesDescriptionIs(Mockito.anyString(), Mockito.anyString())).thenReturn(assembly);
		Boolean returnedFromVote = assemblyService.vote(issue.getOwner().getCpf(), issue.getDescription(), createSimpleOwner());
		assertThat(returnedFromVote).isEqualTo(Boolean.TRUE);
	}
	
	@Test
	public void testIncorrectVote() {
		Assembly assembly = createSimpleAssembly();
		Issue issue = assembly.getIssues().iterator().next();
		Mockito.when(assemblyRepository.findByIssuesOwnerCpfIsAndIssuesDescriptionIs(Mockito.anyString(), Mockito.anyString())).thenReturn(assembly);
		Boolean returnedFromVote = assemblyService.vote(issue.getOwner().getCpf(), issue.getDescription(), createSimpleOwner());
		assertThat(returnedFromVote).isEqualTo(Boolean.TRUE);
	}
	
	@Test
	public void testGetAllEnable() {
		
	}
	
	@Test
	public void testGetAllDisable() {
		
	}
	
	
	
	

}
