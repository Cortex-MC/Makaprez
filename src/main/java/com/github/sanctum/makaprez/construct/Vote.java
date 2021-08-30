package com.github.sanctum.makaprez.construct;

import com.github.sanctum.makaprez.api.ElectionProperty;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Vote implements ElectionProperty<Candidate>, Serializable {

	private final UUID voter;

	private final UUID candidate;

	public Vote(UUID voter, UUID candidate) {
		this.voter = voter;
		this.candidate = candidate;
	}

	public OfflinePlayer getVoter() {
		return Bukkit.getOfflinePlayer(this.voter);
	}

	@Override
	public Candidate getData() {
		return new Candidate(this.candidate);
	}

	@Override
	public Type getType() {
		return Type.VOTE;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vote)) return false;
		Vote vote = (Vote) o;
		return Objects.equals(getVoter(), vote.getVoter()) &&
				Objects.equals(candidate, vote.candidate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getVoter(), candidate);
	}
}
