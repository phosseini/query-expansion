package com.sherlocks.sts.sandbox;

public class QueryScore {
	
	public final double score;
	public final String expandedQuery;
	public final String originalQuery;
	public final String simMeasure;
	
	public QueryScore(double score, String eQuery, String oQuery, String sim) {
		this.score = score;
		
		if (eQuery == null) {
			System.out.println("There was a problem with getting the query.");
			this.expandedQuery = "";
		} else {
			this.expandedQuery = eQuery;
		}
		
		if (oQuery == null) {
			System.out.println("There was a problem with getting the query.");
			this.originalQuery = "";
		} else {
			this.originalQuery = oQuery;
		}
		
		this.simMeasure = sim;
	}
}
