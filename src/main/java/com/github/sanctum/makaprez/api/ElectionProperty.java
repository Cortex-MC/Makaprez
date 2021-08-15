package com.github.sanctum.makaprez.api;

public interface ElectionProperty<T> {

	T getData();

	default Type getType() {
		return Type.UNKNOWN;
	}

	enum Type {
		VOTE, BODY, UNKNOWN;
	}

}
