package com.github.sanctum.makaprez.api;

import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class CommandBuilder extends Command {

	protected CommandBuilder(String label) {
		super(label);
	}

	public abstract boolean apply(Player p, String label, String[] args);

	public boolean apply(CommandSender sender, String label, String[] args) {
		return false;
	}

	@Override
	public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
		if (!(sender instanceof Player)) {
			return apply(sender, commandLabel, args);
		}
		return apply((Player)sender, commandLabel, args);
	}

	public abstract List<String> complete(Player p, String alias, String[] args);


}
