package com.github.sanctum.makaprez.command;

import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.library.Message;
import com.github.sanctum.makaprez.api.CommandBuilder;
import com.github.sanctum.makaprez.api.Makaprez;
import com.github.sanctum.makaprez.construct.Election;
import com.github.sanctum.makaprez.event.PollRenderDisplayEvent;
import java.util.List;
import org.bukkit.entity.Player;

public class CommandElection extends CommandBuilder {

	public CommandElection() {
		super("election");
	}

	@Override
	public boolean apply(Player p, String label, String[] args) {
		Message m = Message.form(p);
		if (args.length == 0) {
			Election e = Makaprez.getInstance().getElection();
			PollRenderDisplayEvent event = new Vent.Call<>(new PollRenderDisplayEvent(e.electoral(), p)).run();
			if (!event.isCancelled()) {
				m.send("&aCurrent poll status:");
				m.send("&f&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				for (String s : Makaprez.getInstance().getElection().getRenderer().getResult()) {
					m.send(s);
				}
				m.send("&f&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
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
