package com.github.sanctum.makaprez.event;

import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.construct.Election;

public abstract class ElectionEvent extends Vent {

	private final Election election;

	public ElectionEvent() {
		this.election = Makaprez.getInstance().getElection();
	}

	public Election getElection() {
		return this.election;
	}

	public boolean isTired() {
		return this instanceof TiredElectionEvent;
	}

}
