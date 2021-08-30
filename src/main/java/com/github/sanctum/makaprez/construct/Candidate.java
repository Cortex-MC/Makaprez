package com.github.sanctum.makaprez.construct;

import com.github.sanctum.makaprez.api.ElectionProperty;
import java.io.Serializable;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Candidate implements ElectionProperty<OfflinePlayer>, Serializable {

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
