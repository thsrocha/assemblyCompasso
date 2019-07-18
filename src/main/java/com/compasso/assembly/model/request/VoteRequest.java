package com.compasso.assembly.model.request;

import com.compasso.assembly.model.Person;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {
	
	private String issueOwnerCpf;
	private String issueDescription;
	private Person personWhoWantToVote;

}
