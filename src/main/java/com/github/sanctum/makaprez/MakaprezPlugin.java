package com.github.sanctum.makaprez;

import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.command.CommandRegistration;
import com.github.sanctum.labyrinth.data.Registry;
import com.github.sanctum.labyrinth.event.custom.Subscribe;
import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.formatting.string.GradientColor;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.labyrinth.library.TextLib;
import com.github.sanctum.labyrinth.library.VaultPlayer;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.construct.Candidate;
import com.github.sanctum.makaprez.construct.Election;
import com.github.sanctum.makaprez.construct.President;
import com.github.sanctum.makaprez.event.ActiveElectionEvent;
import com.github.sanctum.makaprez.event.ElectionEvent;
import com.github.sanctum.makaprez.event.PollRenderDisplayEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class MakaprezPlugin extends JavaPlugin implements Makaprez {

	private President president;

	private Election election;

	@Override
	public void onEnable() {
		// Plugin startup logic
		Bukkit.getServicesManager().register(Makaprez.class, this, this, ServicePriority.Normal);
		election = new Election(10);

		UUID pres = LabyrinthProvider.getInstance().getContainer(election.getNamespace()).get(UUID.class, "president");

		if (pres != null) {
			president = new President().setPresident(pres);
		} else {
			president = new President();
			election.start();
		}

		new Registry<>(Command.class).source(this).pick("com.github.sanctum.makaprez.command").operate(CommandRegistration::use);

		Vent.register(this, this);

	}

	@Subscribe(priority = Vent.Priority.LOW)
	public void onDisplay(PollRenderDisplayEvent e) {
		e.setCancelled(true);
		Map<String, Candidate> candidateMap = new HashMap<>();
		for (Candidate c : e.getCandidates()) {
			char chara = c.getData().getName().charAt(0);
			candidateMap.put(String.valueOf(chara).toUpperCase(), c);
		}
		Message m = Message.form(e.getRenderer());
		String border = "&f&l&m▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";
		m.send("&b2021 Electoral status:");
		m.send(border);
		m.send(" ");
		Map<Candidate, GradientColor> colorMap = new HashMap<>();
		for (Map.Entry<String, Candidate> entry : candidateMap.entrySet()) {
			StringBuilder bars = new StringBuilder("⬛");
			for (int i = 0; i < e.getElection().count(entry.getValue()) + 1; i++) {
				bars.append("⬛");
			}
			Random random = new Random();
			String color1 = String.format("#%06x", random.nextInt(0xffffff + 1));
			String color2 = String.format("#%06x", random.nextInt(0xffffff + 1));
			GradientColor color = new GradientColor(color1, color2);
			colorMap.put(entry.getValue(), color);
			m.build(TextLib.getInstance().textHoverable("(" + colorMap.get(entry.getValue()).context(":" + entry.getKey() + ":").translate() + "&f) &7&l| ", color.context(bars.toString()).translate(), "&6&l" + color.context(entry.getValue().getData().getName()).translate() + "&f: (&b" + election.count(entry.getValue()) + "&f)"));
		}
		m.send(" ");
		m.send(border);
	}

	@Subscribe
	public void onElection(ActiveElectionEvent e) {
		if (Bukkit.getOnlinePlayers().size() == 0) return;
		for (Candidate c : e.getElection().top(5)) {
			if (e.getElection().count(c) >= 10) {
				e.getElection().stop();
				Message.loggedFor(JavaPlugin.getProvidingPlugin(MakaprezPlugin.class)).broadcast("&eSay hello to president &6&l" + c.getData().getName());
				if (getPresident().isSet()) {
					String previous = LabyrinthProvider.getInstance().getContainer(election.getNamespace()).get(String.class, "rank");
					if (previous != null) {
						for (String w : Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toSet())) {
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usetg " + getPresident().getPuppet().getName() + " " + w + " " + previous);
						}
					}
				}

				LabyrinthProvider.getInstance().getContainer(election.getNamespace()).attach("president", c.getData().getUniqueId());
				LabyrinthProvider.getInstance().getContainer(election.getNamespace()).attach("rank", VaultPlayer.wrap(c.getData()).getGroup(Bukkit.getWorlds().get(0).getName()).getName());
				for (String w : Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toSet())) {
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "usetg " + c.getData().getName() + " " + w + " President");
				}
				getPresident().setPresident(c.getData().getUniqueId());
				e.getElection().clear();
			}
		}
	}

	@Subscribe(priority = Vent.Priority.READ_ONLY)
	public void onElection(ElectionEvent e) {
		if (e.isTired()) {
			if (Bukkit.getOnlinePlayers().size() > 0) {
				if (e.getElection().isPaused()) {
					e.getElection().start();
				}
			}
		} else {
			if (Bukkit.getOnlinePlayers().size() == 0) {
				if (!e.getElection().isPaused()) {
					e.getElection().pause();
				}
			}
		}
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
		getElection().stop();
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
