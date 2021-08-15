package com.github.sanctum.makaprez.api;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Candidate implements ElectionProperty<OfflinePlayer>{

	private final UUID id;

	public Candidate(UUID ID) {
		this.id = ID;
	}

	@Override
	public OfflinePlayer getData() {
		return Bukkit.getOfflinePlayer(this.id);
	}


	@Override
	public Type getType() {
		return Type.BODY;
	}
}
