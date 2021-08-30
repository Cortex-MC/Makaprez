package com.github.sanctum.makaprez.api;

import com.github.sanctum.makaprez.construct.Election;
import com.github.sanctum.makaprez.construct.President;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public interface Makaprez {

	static Makaprez getInstance() {
		return Bukkit.getServicesManager().load(Makaprez.class);
	}

	@NotNull President getPresident();

	@NotNull Election getElection();

}
