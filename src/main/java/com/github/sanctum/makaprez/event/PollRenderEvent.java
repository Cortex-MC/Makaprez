package com.github.sanctum.makaprez.event;

import com.github.sanctum.makaprez.construct.Candidate;

public class PollRenderEvent extends ElectionEvent {

	private final Candidate candidate;
	private final int progress;
	private String format;


	public PollRenderEvent(Candidate candidate, int progress) {
		super();
		this.candidate = candidate;
		this.progress = progress;
	}

	public int getProgress() {
		return progress;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Candidate getCandidate() {
		return candidate;
	}


}
