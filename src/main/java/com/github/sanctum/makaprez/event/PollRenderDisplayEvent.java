package com.github.sanctum.makaprez.event;

import com.github.sanctum.makaprez.construct.Candidate;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.entity.Player;

public class PollRenderDisplayEvent extends ElectionEvent {

	private final Set<Candidate> candidates;
	private final Player player;

	public PollRenderDisplayEvent(Set<Candidate> candidates, Player player) {
		super();
		this.player = player;
		this.candidates = new HashSet<>(candidates);
	}

	public Player getRenderer() {
		return this.player;
	}

	public Set<Candidate> getCandidates() {
		return candidates;
	}
}
