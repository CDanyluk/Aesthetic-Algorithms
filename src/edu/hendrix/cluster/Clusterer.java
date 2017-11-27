package edu.hendrix.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.hendrix.util.Duple;
import edu.hendrix.util.Util;

public interface Clusterer<C extends Clusterable<C>> {
	public int train(C example);
	
	default public int getClosestMatchFor(C example) {
		return getClosestNodeDistanceFor(example).getFirst();
	}
	
	default public C getMatchingIdealInputFor(C example) {
		return getIdealInputFor(getClosestMatchFor(example));
	}
	
	default public Duple<Integer, Double> getClosestNodeDistanceFor(C example) {
		Util.assertArgument(example != null, "Null example given!");
		Duple<Integer,Double> result = null;
		for (int id: getClusterIds()) {
			double dist = example.distance(getIdealInputFor(id));
			if (result == null || dist < result.getSecond()) {
				result = new Duple<>(id, dist);
			}
		}
		Util.assertState(result != null, "Returning null result!");
		return result;
	}
	
	default public ArrayList<Duple<Integer, Double>> getNodeRanking(C example) {
		ArrayList<Duple<Integer, Double>> result = new ArrayList<>();
		for (int id: getClusterIds()) {
			result.add(new Duple<>(id, example.distance(getIdealInputFor(id))));
		}
		Collections.sort(result, (o1, o2) -> o1.getSecond() < o2.getSecond() ? -1 : o1.getSecond() > o2.getSecond() ? 1 : 0);
		return result;
	}
	
	default public ArrayList<C> getAllIdealInputs() {
		ArrayList<C> result = new ArrayList<>();
		for (int id: getClusterIds()) {
			result.add(getIdealInputFor(id));
		}
		return result;
	}
	
	public C getIdealInputFor(int node);
	
	public int size();
	
	public Collection<Integer> getClusterIds();
}
