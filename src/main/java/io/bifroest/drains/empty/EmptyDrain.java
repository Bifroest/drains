package io.bifroest.drains.empty;

import java.util.List;

import io.bifroest.commons.model.Metric;
import io.bifroest.drains.AbstractBasicDrain;

public class EmptyDrain extends AbstractBasicDrain {
	public EmptyDrain() {
	}

	@Override
	public void output( List<Metric> metrics ) {
		// Whoosh
	}
}
