package com.github.sanctum.makaprez.api;

import java.util.List;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public interface Makaprez {

	static Makaprez getInstance() {
		return Bukkit.getServicesManager().load(Makaprez.class);
	}

	@NotNull President getPresident();

	@NotNull Election getElection();

}
