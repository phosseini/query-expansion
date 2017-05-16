package com.sherlocks.sts.sandbox;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.ngrams.WordNGramJaccardMeasure;

public class SimilarityTest {

	public static void main(String[] args) {
		
		File queryFile = new File("C:\\Users\\Rohgaa\\Documents\\GradSchool\\CSCI_6907\\query_dataset.txt");
		SherlocksSemanticTextSimilarity textSim = new SherlocksSemanticTextSimilarity(queryFile);
		ArrayList<QueryScore[]> topQueries = textSim.performQueryComparisons();
		
		String outputPath = new String("C:\\Users\\Rohgaa\\Documents\\GradSchool\\CSCI_6907\\top_queries.csv");
		
		File returnFile = new File(outputPath);
		
		try {
		
			if (!returnFile.exists()) {
				returnFile.createNewFile();
			}
		
			BufferedWriter writer = new BufferedWriter(new FileWriter(returnFile));
			
			writer.write("Original Query, Word NGram Jaccard, Greedy String Tiling, Longest Common Subsequence, Levenshtein, Cosine");
			writer.newLine();
			
			for (QueryScore[] q : topQueries) {
				writer.write(q[0].originalQuery + ",");
				for (QueryScore score : q) {
					
					if(score.simMeasure.equals("Cosine")) {
						writer.write(score.expandedQuery);
					} else {
						writer.write(score.expandedQuery + ",");
					}
				}
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		testSampleCode();

	}
	
	/**
	 * Method to do impromptu testing of similarity measures.
	 */
	public static void testSampleCode() {
		
		TextSimilarityMeasure measure = new WordNGramJaccardMeasure(2);

		String[] tokens1 = "program chair".split(" ");   
		String[] tokens2 = "plan chair".split(" ");
		String[] tokens3 = "program chair".split(" ");

		double scoreOne = 0.0;
		double scoreTwo = 0.0;
		try {
			scoreOne = measure.getSimilarity(tokens1, tokens2);
			scoreTwo = measure.getSimilarity(tokens1, tokens3);
		} catch (SimilarityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Similarity: " + scoreOne);
		System.out.println("Similarity: " + scoreTwo);
	}

}
