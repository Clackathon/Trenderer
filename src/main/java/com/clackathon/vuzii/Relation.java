package com.clackathon.vuzii;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Euan
 */
public class Relation {




	public double relativity(Image a, Image b){
		List<Double> percentages = new ArrayList<>();

		percentages.add(getTagRelation(a.getUserTags(), b.getUserTags()));
		percentages.add(getUploaderRelation(a.getUploader(), b.getUploader()));
		// percentages.add(getCreationTimeRelation(a.getCreationTime(), b.getCreationTime()));
		percentages.add(getLocationRelation(a.getLocation(), b.getLocation()));
		percentages.add(getCommentsRelation(a.getComments(), b.getComments()));



		/*
			tags 0.4
			uploader 0.8
			creationTime 0.9
			location 0.6
			comments check for hash-tags/similar words 0.8
			location of likes (javascript to check)

		 */

		return calculateAverage(percentages);
	}

	private double getCommentsRelation(List<String> commentsA, List<String> commentsB) {
		List<String> intersection = new ArrayList<>(commentsA);
		intersection.retainAll(commentsB);
		return ((double) intersection.size())/Math.max(commentsA.size(),commentsB.size());
	}

	private double getLocationRelation(Location a, Location b) {
		double longDistance = a.getLongitude() - b.getLongitude();
		double latDistance = Math.abs(a.getLatitude() - b.getLatitude());
		return 1.0 / Math.sqrt(Math.sqrt(longDistance * longDistance + latDistance * latDistance));
	}

	/*private Double getCreationTimeRelation(Date creationTime, Date creationTime1) {
		return
	}
	*/

	private double getUploaderRelation(User uploaderA, User uploaderB) {
		return uploaderA.getName().equals(uploaderB.getName()) ? 1 : 0;
	}

	private double getTagRelation(List<String> a, List<String> b) {
		List<String> intersection = new ArrayList<>(a);
		intersection.retainAll(b);
		return ((double) intersection.size())/Math.max(a.size(),b.size());
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
