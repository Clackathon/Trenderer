package com.clackathon.vuzii;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Euan
 */
public class Relation {

	public double relativity(){
		double relative = 0;

		Image imageA;
		Image imageB;
		List<Double> percentages = new ArrayList<>();
		/*
			tags 0.4
			uploader 0.8
			creationTime 0.9
			location 0.6
			comments check for hash-tags/similar words 0.8

		 */

		return calculateAverage(percentages);
	}

	private static double calculateAverage(List<Double> values) {
		double sum = 0;
		if(!values.isEmpty()) {
			for (double value : values) {
				sum += value;
			}
			return sum / values.size();
		}
		return sum;
	}


}
