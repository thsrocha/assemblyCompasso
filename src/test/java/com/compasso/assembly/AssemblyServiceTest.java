package com.compasso.assembly;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.compasso.assembly.enums.StatusObject;
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
	
	@Before
	public void deleteAllFromBase() {
		assemblyRepository.deleteAll();
	}

	
	@Test
	public void testCorrectVote() {
		Assembly assembly = createSimpleAssembly();
		assemblyRepository.save(assembly);
		Issue issue = assembly.getIssues().iterator().next();
		Boolean returnedFromVote = assemblyService.vote(issue.getOwner().getCpf(), issue.getDescription(), createSimpleOwner());
		assertThat(returnedFromVote).isEqualTo(Boolean.TRUE);
	}
	
	@Test
	public void testIncorrectVote() {
		Assembly assembly = createSimpleAssembly();
		assemblyRepository.save(assembly);
		Issue issue = (Issue) assembly.getIssues().toArray()[1];
		Boolean returnedFromVote = assemblyService.vote(issue.getOwner().getCpf(), issue.getDescription(), issue.getVotes().iterator().next());
		assertThat(returnedFromVote).isEqualTo(Boolean.FALSE);
	}
	
	@Test
	public void testGetAllActive() {
		Collection<Issue> issuesStatusDeactivated = createListOfIssue();
		issuesStatusDeactivated.forEach(item -> {
			item.setStatusObject(StatusObject.DESACTIVATE);
		});
		
		Assembly assemblyWithListOfIssuesDeactivate = createSimpleAssembly();
		assemblyWithListOfIssuesDeactivate.setIssues(issuesStatusDeactivated);
		assemblyWithListOfIssuesDeactivate = assemblyRepository.save(assemblyWithListOfIssuesDeactivate);
		
		Assembly assemblyWithListOfIssuesActivated = createSimpleAssembly();
		assemblyWithListOfIssuesActivated = assemblyRepository.save(assemblyWithListOfIssuesActivated);
		
		Assembly assemblyWithListOfIssuesActivated2 = createSimpleAssembly();
		assemblyWithListOfIssuesActivated2 = assemblyRepository.save(assemblyWithListOfIssuesActivated2);
		
		Collection<Assembly> onlyDesactivate = assemblyService.getAllAssemblyByStatus(StatusObject.ACTIVE);
		for (Assembly assembly : onlyDesactivate) {
			assertThat(assembly.getStatusObject()).isEqualTo(StatusObject.ACTIVE);
			Collection<Issue> issuesActive = assembly.getIssues().stream().filter(issue -> 
																			issue.getStatusObject().equals(StatusObject.DESACTIVATE) ||
																			issue.getStatusObject().equals(StatusObject.FINISH))
																		  .collect(Collectors.toList());
			assertThat(issuesActive.size()).isEqualTo(0);
		}
		assertThat(onlyDesactivate.size()).isEqualTo(2);
	}
	
	
	@Test
	public void testGetAllDesactivate() {
		Collection<Issue> issuesStatusDeactivated = createListOfIssue();
		issuesStatusDeactivated.forEach(item -> {
			item.setStatusObject(StatusObject.DESACTIVATE);
		});
		
		Assembly assemblyWithListOfIssuesDeactivate = createSimpleAssembly();
		assemblyWithListOfIssuesDeactivate.setStatusObject(StatusObject.DESACTIVATE);
		assemblyWithListOfIssuesDeactivate.setIssues(issuesStatusDeactivated);
		assemblyWithListOfIssuesDeactivate = assemblyRepository.save(assemblyWithListOfIssuesDeactivate);
		
		Assembly assemblyWithListOfIssuesActivated = createSimpleAssembly();
		assemblyWithListOfIssuesActivated = assemblyRepository.save(assemblyWithListOfIssuesActivated);
		
		Assembly assemblyWithListOfIssuesActivatedWithOneIssueDesactivate = createSimpleAssembly();
		Issue onlyIssueDesactivated = (Issue) assemblyWithListOfIssuesActivatedWithOneIssueDesactivate.getIssues().toArray()[1];
		onlyIssueDesactivated.setStatusObject(StatusObject.DESACTIVATE);
		assemblyWithListOfIssuesActivatedWithOneIssueDesactivate = assemblyRepository.save(assemblyWithListOfIssuesActivatedWithOneIssueDesactivate);
		
		Collection<Assembly> onlyDesactivate = assemblyService.getAllAssemblyByStatus(StatusObject.DESACTIVATE);
		for (Assembly assembly : onlyDesactivate) {
			assertThat(assembly.getStatusObject()).isEqualTo(StatusObject.DESACTIVATE);
			Collection<Issue> issuesActive = assembly.getIssues().stream().filter(issue -> 
																			issue.getStatusObject().equals(StatusObject.ACTIVE) ||
																			issue.getStatusObject().equals(StatusObject.FINISH))
																		  .collect(Collectors.toList());
			assertThat(issuesActive.size()).isEqualTo(0);
		}
		assertThat(onlyDesactivate.size()).isEqualTo(1);
	}
	
	@Test
	public void testGetAllByStatusActiveIfOneIssueHasStatusOfDesactive() {
		
		Collection<Issue> issuesStatusDeactivated = createListOfIssue();
		issuesStatusDeactivated.forEach(item -> {
			item.setStatusObject(StatusObject.DESACTIVATE);
		});
		
		Assembly assemblyWithListOfIssuesDeactivate = createSimpleAssembly();
		assemblyWithListOfIssuesDeactivate.setStatusObject(StatusObject.DESACTIVATE);
		assemblyWithListOfIssuesDeactivate.setIssues(issuesStatusDeactivated);
		assemblyWithListOfIssuesDeactivate = assemblyRepository.save(assemblyWithListOfIssuesDeactivate);
		
		Assembly assemblyWithListOfIssuesActivated = createSimpleAssembly();
		assemblyWithListOfIssuesActivated = assemblyRepository.save(assemblyWithListOfIssuesActivated);
		
		Assembly assemblyWithListOfIssuesActivatedWithOneIssueDesactivate = createSimpleAssembly();
		Issue onlyIssueDesactivated = (Issue) assemblyWithListOfIssuesActivatedWithOneIssueDesactivate.getIssues().toArray()[1];
		onlyIssueDesactivated.setStatusObject(StatusObject.DESACTIVATE);
		assemblyWithListOfIssuesActivatedWithOneIssueDesactivate = assemblyRepository.save(assemblyWithListOfIssuesActivatedWithOneIssueDesactivate);
		
		Collection<Assembly> onlyDesactivate = assemblyService.getAllAssemblyByStatus(StatusObject.ACTIVE);
		for (Assembly assembly : onlyDesactivate) {
			assertThat(assembly.getStatusObject()).isEqualTo(StatusObject.ACTIVE);
		}
		assertThat(onlyDesactivate.size()).isEqualTo(2);
		
	}
	
	
	
	

}
