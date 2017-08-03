package com.atp.indicators;

import com.atp.Tickable;

public interface Indicator extends Tickable {
	String getName();
	abstract boolean isValid();
	abstract void reset();
}
