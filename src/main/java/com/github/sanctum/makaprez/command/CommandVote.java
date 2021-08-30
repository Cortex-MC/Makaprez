package com.github.sanctum.makaprez.command;

import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.makaprez.api.CommandBuilder;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.construct.Vote;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CommandVote extends CommandBuilder {
	public CommandVote() {
		super("vote");
	}

	@Override
	public boolean apply(Player p, String label, String[] args) {
		if (args.length == 0) {
			OfflinePlayer n = Arrays.stream(Bukkit.getOfflinePlayers()).filter(pl -> pl.getName().equalsIgnoreCase("Hempfest")).findFirst().orElse(null);

			if (n != null) {

				if (!Makaprez.getInstance().getElection().isPaused()) {
					if (Makaprez.getInstance().getElection().vote(new Vote(n.getUniqueId(), p.getUniqueId()))) {
						Message.form(p).send("&6Your vote for player " + n.getName() + " went through!");
					} else {
						Message.form(p).send("&cSomething went wrong, unable to proceed. Perhaps you already voted?");
					}
				} else {
					Message.form(p).send("&cTis not the season for voting right now... Try again later.");
				}

			}
			return true;
		}

		if (args.length == 1) {
			String target = args[0];

			OfflinePlayer n = Arrays.stream(Bukkit.getOfflinePlayers()).filter(pl -> pl.getName().equalsIgnoreCase(target)).findFirst().orElse(null);

			if (n != null) {

				if (!Makaprez.getInstance().getElection().isPaused()) {
					if (Makaprez.getInstance().getElection().vote(new Vote(p.getUniqueId(), n.getUniqueId()))) {
						Message.form(p).send("&6Your vote for player " + n.getName() + " went through!");
					} else {
						Message.form(p).send("&cSomething went wrong, unable to proceed. Perhaps you already voted?");
					}
				} else {
					Message.form(p).send("&cTis not the season for voting right now... Try again later.");
				}

			}

			return true;
		}

		return true;
	}

	@Override
	public List<String> complete(Player p, String alias, String[] args) {
		return null;
	}
}
