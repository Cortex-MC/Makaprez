package com.github.sanctum.makaprez.construct;

import com.github.sanctum.labyrinth.event.custom.Vent;
import com.github.sanctum.labyrinth.formatting.string.DefaultColor;
import com.github.sanctum.makaprez.event.PollRenderEvent;
import java.util.ArrayList;
import java.util.List;

public class PollRenderer {

	private final Election election;

	public PollRenderer(Election election) {
		this.election = election;
	}

	public List<String> getResult() {
		List<String> l = new ArrayList<>();
		for (Candidate candidate : election.top(5)) {
			StringBuilder bars = new StringBuilder("|");
			for (int i = 0; i < election.count(candidate) + 1; i++) {
				bars.append("|");
			}
			PollRenderEvent instance = new PollRenderEvent(candidate, election.count(candidate));
			instance.setFormat(candidate.getData().getName() + " : " + DefaultColor.MANGO.wrap(bars.toString()).translate() + " &f(" + election.count(candidate) + ")");
			PollRenderEvent event = new Vent.Call<>(instance).run();
			l.add(event.getFormat());
		}
		return l;
	}


}
