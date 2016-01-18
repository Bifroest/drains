package com.goodgame.profiling.drains.empty;

import java.util.List;

import com.goodgame.profiling.drains.AbstractBasicDrain;

import io.bifroest.commons.model.Metric;

public class EmptyDrain extends AbstractBasicDrain {
	public EmptyDrain() {
	}

	@Override
	public void output( List<Metric> metrics ) {
		// Whoosh
	}
}
