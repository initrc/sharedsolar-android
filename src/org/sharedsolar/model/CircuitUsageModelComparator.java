package org.sharedsolar.model;

import java.util.Comparator;

public class CircuitUsageModelComparator implements Comparator<CircuitUsageModel>{
	public int compare(CircuitUsageModel object1, CircuitUsageModel object2) {
		return object1.getAid().compareTo(object2.getAid());
	}
}
