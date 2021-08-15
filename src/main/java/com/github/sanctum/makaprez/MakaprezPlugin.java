package com.github.sanctum.makaprez;

import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.command.CommandBuilder;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.makaprez.api.Election;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.api.President;
import com.github.sanctum.makaprez.api.Vote;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MakaprezPlugin extends JavaPlugin implements Makaprez {

	private President president;

	private Election election;

	@Override
	public void onEnable() {
		// Plugin startup logic

		election = new Election();

		UUID pres = LabyrinthProvider.getInstance().getContainer(election.getNamespace()).get(UUID.class, "president");

		if (pres != null) {
			president = new President().setPresident(pres);
		} else {
			president = new President();
		}

		CommandBuilder.use(this)
				.label("vote")
				.explain("A command to vote in an election.")
				.example("/vote <Player>")
				.player((p, label, args) -> {

					if (args.length == 0) {

						return true;
					}

					if (args.length == 1) {
						String target = args[0];

						Player n = Bukkit.getOnlinePlayers().stream().filter(pl -> pl.getName().equalsIgnoreCase(target)).findFirst().orElse(null);

						if (n != null) {

							if (!Election.isPaused()) {
								if (getElection().vote(new Vote(p.getUniqueId(), n.getUniqueId()))) {
									Message.form(p).send("&6Your vote for player " + n.getName() + " went through!");
								}
							} else {
								Message.form(p).send("&cTis not the season for voting right now... Try again later.");
							}

						}

						return true;
					}

					return true;
				})
				.console((sender, label, args) -> true)
				.tab((p, args, builder) -> builder);

	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}

	@Override
	public @NotNull President getPresident() {
		return president;
	}

	@Override
	public @NotNull Election getElection() {
		return election;
	}


}
