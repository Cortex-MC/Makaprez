package com.github.sanctum.makaprez.api;

import com.github.sanctum.labyrinth.formatting.string.GradientColor;
import com.github.sanctum.labyrinth.library.StringUtils;
import com.github.sanctum.labyrinth.library.VaultPlayer;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class President {
	private String c1;
	private String c2;
	private UUID president;
	private double percentage;

	public President setPresident(UUID president) {
		this.president = president;
		return this;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public OfflinePlayer getPuppet() {
		return Bukkit.getOfflinePlayer(this.president);
	}

	public VaultPlayer getPlayer() {
		return VaultPlayer.wrap(getPuppet());
	}

	public void setColor1(String c1) {
		this.c1 = c1;
	}

	public void setColor2(String c2) {
		this.c2 = c2;
	}

	public GradientColor getColors() {
		return StringUtils.use(getPuppet().getName()).modifiableGradient(this.c1, this.c2);
	}

}
