package com.github.sanctum.makaprez;

import com.github.sanctum.labyrinth.LabyrinthProvider;
import com.github.sanctum.labyrinth.command.CommandRegistration;
import com.github.sanctum.labyrinth.data.Registry;
import com.github.sanctum.labyrinth.event.custom.Subscribe;
import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.formatting.string.DefaultColor;
import com.github.sanctum.labyrinth.formatting.string.GradientColor;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.construct.Candidate;
import com.github.sanctum.makaprez.construct.Election;
import com.github.sanctum.makaprez.construct.President;
import com.github.sanctum.makaprez.event.ElectionEvent;
import com.github.sanctum.makaprez.event.PollRenderDisplayEvent;
import com.github.sanctum.makaprez.event.PollRenderEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
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
		election = new Election();
		election.start();

		UUID pres = LabyrinthProvider.getInstance().getContainer(election.getNamespace()).get(UUID.class, "president");

		if (pres != null) {
			president = new President().setPresident(pres);
		} else {
			president = new President();
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
			m.send("(" + colorMap.get(entry.getValue()) + entry.getKey() + "&f) &7&l| " + color.context(bars.toString()).translate());
		}
		StringBuilder footer = new StringBuilder();
		candidateMap.forEach((key, value) -> footer.append(colorMap.get(value)).append(key).append("&r").append(" = ").append(value.getData().getName()).append(" &7(").append(e.getElection().count(value)).append(")").append("&r, "));
		m.send(" ");
		m.send(border);
		m.send(footer.toString());
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
