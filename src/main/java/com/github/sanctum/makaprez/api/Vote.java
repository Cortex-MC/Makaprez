package com.github.sanctum.makaprez.api;

import java.io.Serializable;
import java.util.UUID;

public class Vote implements ElectionProperty<Candidate>, Serializable {

	private final UUID voter;

	private final UUID candidate;

	public Vote(UUID voter, UUID candidate) {
		this.voter = voter;
		this.candidate = candidate;
	}

	public UUID getVoter() {
		return voter;
	}

	@Override
	public Candidate getData() {
		return new Candidate(this.candidate);
	}

	@Override
	public Type getType() {
		return Type.VOTE;
	}
}
