package edu.hendrix.blob;

import java.util.ArrayList;

public class DisjointSets {
	private ArrayList<Node> nodes;
	
	private class Node {
		int name, rank;
		Node parent;
		
		Node(int name) {
			this.name = name;
			this.parent = this;
			this.rank = 0;
		}
	}
	
	public DisjointSets() {
		nodes = new ArrayList<Node>();
	}
	
	public int makeSet() {
		int name = size();
		nodes.add(new Node(name));
		return name;
	}
	
	public int size() {
		return nodes.size();
	}
	
	public int find(int name) {
		Node start = nodes.get(name);
		while (start != start.parent) {
			start = start.parent;
		}
		return start.name;
	}
	
	public void union(int name1, int name2) {
		Node root1 = nodes.get(find(name1));
		Node root2 = nodes.get(find(name2));
		if (root1 != root2) {
			if (root1.rank > root2.rank) {
				root2.parent = root1;
			} else {
				root1.parent = root2;
				if (root1.rank == root2.rank) {
					root2.rank += 1;
				}
			}
		}
	}
}
