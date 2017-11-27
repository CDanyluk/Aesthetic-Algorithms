package edu.hendrix.cluster;

import java.util.ArrayList;

import edu.hendrix.util.Distribution;
import edu.hendrix.util.Util;

public class KMeansPlusPlus<C extends Clusterable<C>> implements Clusterer<C> {

	private ArrayList<C> examples;
	private ArrayList<C> clusters;
	private int k;

	public KMeansPlusPlus(int k) {
		this.k = k;
		examples = new ArrayList<>();
		clusters = new ArrayList<>();
	}

	public void performTraining() {
		Util.assertState(!trained(), "Already trained");
		Util.assertState(k <= examples.size(), "Not enough examples for " + k + " clusters.");
		
		initKPlusPlusClusters();
		kMeans();
	}

	private void initKPlusPlusClusters() {
		ArrayList<C> candidates = new ArrayList<>(examples);
		clusters.add(candidates.remove((int)(Math.random() * candidates.size())));
		while (clusters.size() < k) {
			Distribution d = new Distribution(makeDistroFrom(clusters, candidates));
			clusters.add(candidates.remove(d.pick()));		
		}
	}

	public double[] makeDistroFrom(ArrayList<C> clusters, ArrayList<C> candidates) {
		double[] result = new double[candidates.size()];
		for (int i = 0; i < candidates.size(); i++) {
			C candidate = candidates.get(i);
			result[i] = 0;
			for (C cluster: clusters) {
				double distance = candidate.distance(cluster);
				if (distance > result[i]) {
					result[i] = distance;
				}
			}
			result[i] = Math.pow(result[i], 2);
		}
		return result;
	}

	@Override
	public int train(C example) {
		Util.assertState(!trained(), "Already trained");
		examples.add(example);
		return -1;
	}

	@Override
	public C getIdealInputFor(int node) {
		if (!trained()) {
			performTraining();
		}
		return clusters.get(node);
	}

	public boolean trained() {
		return size() > 0;
	}

	@Override
	public int size() {
		return clusters.size();
	}

	@Override
	public ArrayList<Integer> getClusterIds() {
		if (!trained()) {
			performTraining();
		}
		ArrayList<Integer> ids = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			ids.add(i);
		}
		return ids;
	}

	private void kMeans() {
		ArrayList<ArrayList<C>> matches = makeEmptyMatches();
		for (C example: examples) {
			matches.get(findClosest(example)).add(example);
		}
		boolean moved = true;
		while (moved) {
			rebuildClusters(matches);
			ArrayList<ArrayList<C>> updated = makeEmptyMatches();
			moved = false;
			for (int i = 0; i < matches.size(); i++) {
				for (int j = 0; j < matches.get(i).size(); j++) {
					C example = matches.get(i).get(j);
					int best = findClosest(example);
					if (best != i) {
						moved = true;
					}
					updated.get(best).add(example);
				}
			}
			matches = updated;
		}
	}

	private int findClosest(C candidate) {
		int best = 0;
		double bestDist = Double.POSITIVE_INFINITY;
		for (int i = 0; i < clusters.size(); i++) {
			double distance = clusters.get(i).distance(candidate);
			if (distance < bestDist) {
				bestDist = distance;
				best = i;
			}
		}
		return best;
	}

	private ArrayList<ArrayList<C>> makeEmptyMatches() {
		ArrayList<ArrayList<C>> matches = new ArrayList<>();
		for (int i = 0; i < clusters.size(); i++) {
			matches.add(new ArrayList<>());
		}
		return matches;
	}

	private void rebuildClusters(ArrayList<ArrayList<C>> matches) {
		Util.assertArgument(matches.size() == clusters.size(), "Mismatched sizes of clusters and match lists");
		for (int i = 0; i < clusters.size(); i++) {
			clusters.set(i, matches.get(i).size() > 0 
					? Clusterable.clusterOf(matches.get(i)) 
					: examples.get((int)(Math.random() * examples.size())));
		}
	}
}