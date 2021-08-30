package com.github.sanctum.makaprez.construct;

import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.data.container.PersistentContainer;
import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.formatting.PaginatedList;
import com.github.sanctum.labyrinth.library.NamespacedKey;
import com.github.sanctum.labyrinth.task.Schedule;
import com.github.sanctum.labyrinth.task.Synchronous;
import com.github.sanctum.makaprez.MakaprezPlugin;
import com.github.sanctum.makaprez.api.ElectionProperty;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.event.ActiveElectionEvent;
import com.github.sanctum.makaprez.event.ElectionEvent;
import com.github.sanctum.makaprez.event.TiredElectionEvent;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class Election {

	private final List<ElectionProperty<?>> properties = new LinkedList<>();

	private final int max;
	private Synchronous task;
	private final PersistentContainer container;
	private final NamespacedKey key;
	private final PollRenderer render;
	private boolean started;

	public Election(int cap) {
		this.max = cap;
		Plugin pl = JavaPlugin.getProvidingPlugin(MakaprezPlugin.class);
		this.key = new NamespacedKey(pl, "election");
		this.container = LabyrinthProvider.getInstance().getContainer(key);
		List<Vote> votes = (List<Vote>) container.get(List.class, "votes");
		if (votes != null) {
			properties.addAll(votes);
		}
		this.render = new PollRenderer(this);
	}

	public PollRenderer getRenderer() {
		return render;
	}

	public int count(Candidate candidate) {
		int i = 0;
		for (Candidate v : ballet().stream().map(Vote::getData).collect(Collectors.toSet())) {
			if (Objects.equals(v.getData().getName(), candidate.getData().getName())) {
				i++;
			}
		}
		return i;
	}

	public boolean vote(Vote vote) {
		if (isPaused()) return false;
		//if (ballet().stream().anyMatch(v -> v.getVoter().equals(vote.getVoter()))) return false;
		if (count(vote.getData()) >= max) {
			return false;
		}
		/*
		if (ballet().stream().anyMatch(v -> v.equals(vote))) {
			return false;
		}

		 */
		properties.add(vote);
		return true;
	}

	public void start() {
		if (!started) {
			started = true;
			if (this.task == null) {
				this.task = Schedule.sync(() -> {
					if (!Makaprez.getInstance().getElection().isPaused()) {
						new Vent.Call<>(new ActiveElectionEvent()).run();
					} else {
						new Vent.Call<>(new TiredElectionEvent()).run();
					}
				});
				task.repeat(0, 1);
			}
		}
	}

	public void pause() {
		if (started) {
			started = false;
			container.attach("votes", ballet());
		}
	}

	public void stop() {
		if (!started) return;
		pause();
		if (task != null) {
			task.cancelTask();
		}
	}

	public void clear() {
		container.delete("votes");
		properties.clear();
		stop();
		task = null;
	}

	public boolean isPaused() {
		return !started;
	}

	public boolean isStarted() {
		return started;
	}

	public NamespacedKey getNamespace() {
		return key;
	}

	public @NotNull Set<Candidate> electoral() {
		return new HashSet<>(ballet()).stream().map(Vote::getData).collect(Collectors.toSet());
	}

	public @NotNull List<Candidate> top(int height) {
		return new PaginatedList<>(electoral()).limit(height).compare(Comparator.comparingInt(this::count)).get(1);
	}

	public @NotNull List<Vote> ballet() {
		return properties.stream().filter(p -> p instanceof Vote).map(property -> (Vote) property).collect(Collectors.toList());
	}

}
