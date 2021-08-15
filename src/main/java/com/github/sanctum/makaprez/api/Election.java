package com.github.sanctum.makaprez.api;

import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.data.container.PersistentContainer;
import com.github.sanctum.labyrinth.formatting.PaginatedList;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.labyrinth.library.NamespacedKey;
import com.github.sanctum.makaprez.MakaprezPlugin;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Election {

	private final Set<ElectionProperty<?>> properties = new HashSet<>();

	private final int max;

	private final NamespacedKey key;

	private static boolean started;

	private static boolean paused;

	public Election() {
		this.max = 10;

		Plugin pl = JavaPlugin.getProvidingPlugin(MakaprezPlugin.class);

		this.key = new NamespacedKey(pl, "election");

		PersistentContainer container = LabyrinthProvider.getInstance().getContainer(key);
		List<Vote> votes = (List<Vote>) container.get(List.class, "votes");
		if (votes != null) {
			properties.addAll(votes);
		}

	}

	public int count(UUID candidate) {
		int i = 0;

		for (Vote v : ballet()) {
			if (v.getData().getData().getUniqueId().equals(candidate)) {
				i++;
			}
		}
		return i;
	}

	public boolean vote(Vote vote) {

		if (paused) return false;

		if (ballet().size() >= max) {
			started = false;
			paused = true;
			return false;
		}

		if (!properties.add(vote)) {
			Optional.ofNullable(Bukkit.getOfflinePlayer(vote.getVoter()).getPlayer())
					.ifPresent(p -> Message.form(p).send("&c&lYou have already voted!"));
			return false;
		}
		return true;
	}

	public void start() {
		if (paused) {
			paused = false;
			return;
		}
		if (!started) {
			started = true;
		}
	}

	public void pause() {
		if (!paused) {
			paused = true;
			PersistentContainer container = LabyrinthProvider.getInstance().getContainer(key);
			container.attach("votes", ballet());
		}
	}

	public static boolean isPaused() {
		return paused;
	}

	public static boolean isStarted() {
		return started;
	}

	public NamespacedKey getNamespace() {
		return key;
	}

	public @NotNull List<Candidate> electoral() {
		return ballet().stream().map(Vote::getData).collect(Collectors.toList());
	}

	public @NotNull List<Candidate> top(int height) {
		return new PaginatedList<>(electoral()).limit(height).compare(Comparator.comparingInt(o -> count(o.getData().getUniqueId()))).get(1);
	}

	public @NotNull List<Vote> ballet() {
		return properties.stream().filter(p -> p instanceof Vote).map(property -> (Vote) property).collect(Collectors.toList());
	}

}
