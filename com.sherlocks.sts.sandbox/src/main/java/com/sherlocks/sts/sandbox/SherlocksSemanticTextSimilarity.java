package com.sherlocks.sts.sandbox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.ngrams.WordNGramJaccardMeasure;
import dkpro.similarity.algorithms.lexical.string.CosineSimilarity;
import dkpro.similarity.algorithms.lexical.string.GreedyStringTiling;
import dkpro.similarity.algorithms.lexical.string.LevenshteinComparator;
import dkpro.similarity.algorithms.lexical.string.LongestCommonSubsequenceComparator;

/**
 * This is a semantic text similarity system developed for Team Sherlocks for CSCI 6907 - DeepQA in IBM Watson.
 * 
 * @author Rohan Sunder
 *
 */
public class SherlocksSemanticTextSimilarity {
	
	private File queryFile;
	private BufferedReader queryReader;

	/**
	 * Constructor for Semantic Text Similarity system.
	 * @param queryFile
	 * @param expandedQFile
	 */
	public SherlocksSemanticTextSimilarity(File queryFile) {
		
		this.queryFile = queryFile;
		
	}
	
	/**
	 * Loads input file containing queries and their expanded forms.
	 * Reads each line and determines 
	 * @return
	 */
	public ArrayList<QueryScore[]> performQueryComparisons() {
		
		ArrayList<QueryScore[]> topFinalQueries = new ArrayList<QueryScore[]>();
		
		try {
			
			this.queryReader = new BufferedReader(new FileReader(this.queryFile));
			
			// Assume file read is either a csv file or a text file with commas as delimiters differentiating queries.
			String delimiter = ",";
			String line = null;
			
			while ((line = this.queryReader.readLine()) != null) {
				
				// Split read line into several query strings.
				String[] queries = line.split(delimiter);
				
				// First query is the original one. The rest are the expanded queries.
				String originalQuery = queries[1];
				String[] expandedQueries = Arrays.copyOfRange(queries, 2, queries.length);
				
				originalQuery = originalQuery.trim();
				
				QueryScore[] topQueries = this.scoreExpandedQueries(originalQuery, expandedQueries);
				topFinalQueries.add(topQueries);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred opening one of the files.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return topFinalQueries;
	}
	
	/**
	 * Where the magic happens. Runs each set of queries and expanded strings across 
	 * several semantic similarity measure.
	 * @param originalQuery
	 * @param expandedQueries
	 * @return
	 */
	private QueryScore[] scoreExpandedQueries(String originalQuery, String[] expandedQueries) {
		
		
		TextSimilarityMeasure jaccardMeasure = new WordNGramJaccardMeasure(3);
		TextSimilarityMeasure greedyTilingMeasure = new GreedyStringTiling(3);
		TextSimilarityMeasure lcsMeasure = new LongestCommonSubsequenceComparator();
		TextSimilarityMeasure levenMeasure = new LevenshteinComparator();
		TextSimilarityMeasure cosineSimMeasure = new CosineSimilarity();

		double jaccardScore = -1.0, greedyTilingScore = -1.0, levenScore = -1.0, cosineScore = -1.0, lcsScore = -1.0, 
				jaccardMax = -1.0, greedyTilingMax = -1.0, lcsMax = -1.0, levenMax = -1.0, cosineMax = -1.0;
		
		String jaccardString = originalQuery, greedyTilingString = originalQuery, lcsString = originalQuery, 
				levenString = originalQuery, cosineSimString = originalQuery;
		
		// Split original query into separate strings to compute string similarity.
		String[] originalQueryTokens = originalQuery.split(" ");
		
		try {
			
			for (String s : expandedQueries) {
				
				boolean isWhitespace = s.matches("^\\s*$");
				if (isWhitespace || s.equals("")) {
					// Do nothing when the string is just whitespace.
				} else {
					s = s.trim();
					String[] expTokens = s.split(" ");
					
					jaccardScore = jaccardMeasure.getSimilarity(originalQueryTokens, expTokens);
					greedyTilingScore = greedyTilingMeasure.getSimilarity(originalQueryTokens, expTokens);
					lcsScore = lcsMeasure.getSimilarity(originalQueryTokens, expTokens);
					levenScore = levenMeasure.getSimilarity(originalQueryTokens, expTokens);
					cosineScore = cosineSimMeasure.getSimilarity(originalQueryTokens, expTokens);
					
					if (jaccardScore > jaccardMax) {
						jaccardMax = jaccardScore;
						jaccardString = s;
					}
					
					if (greedyTilingScore > greedyTilingMax) {
						greedyTilingMax = greedyTilingScore;
						greedyTilingString = s;
					}
					
					if (lcsScore > lcsMax) {
						lcsMax = lcsScore;
						lcsString = s;
					}
					
					if (levenScore > levenMax) {
						levenMax = levenScore;
						levenString = s;
					}
					
					if (cosineScore > cosineMax) {
						cosineMax = cosineScore;
						cosineSimString = s;
					}
				}
				
			}
			
		} catch (SimilarityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
		}

		ArrayList<QueryScore> bestQueries = new ArrayList<QueryScore>();
		
		bestQueries.add(new QueryScore(jaccardMax, jaccardString, originalQuery, "Jaccard"));
		bestQueries.add(new QueryScore(greedyTilingMax, greedyTilingString, originalQuery, "Greedy String Tiling"));
		bestQueries.add(new QueryScore(lcsMax, lcsString, originalQuery, "Longest Common Subsequence"));
		bestQueries.add(new QueryScore(levenMax, levenString, originalQuery, "Levenshtein"));
		bestQueries.add(new QueryScore(cosineMax, cosineSimString, originalQuery, "Cosine"));
		
		QueryScore[] returnQueries = new QueryScore[bestQueries.size()]; 
		returnQueries = bestQueries.toArray(returnQueries);
		
		System.out.println(returnQueries);
		
		return returnQueries;
	}
	
}
