package com.goodgame.profiling.drains.empty;

import java.util.List;

import com.goodgame.profiling.commons.model.Metric;
import com.goodgame.profiling.drains.AbstractBasicDrain;

public class EmptyDrain extends AbstractBasicDrain {
	public EmptyDrain() {
	}

	@Override
	public void output( List<Metric> metrics ) {
		// Whoosh
	}
}
