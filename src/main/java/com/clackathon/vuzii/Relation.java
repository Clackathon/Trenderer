package com.clackathon.vuzii;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Euan
 */
public class Relation {


	private static final Pattern hashtagPattern = Pattern.compile("#[a-zA-Z0-9]*");

	public double relativity(Image a, Image b){
		List<Double> percentages = new ArrayList<>();

		percentages.add(getTagRelation(a.getCloudVisionTags(), b.getCloudVisionTags()));
//		percentages.add(getTagRelation(a.getUserTags(), b.getUserTags()));
//		percentages.add(getUploaderRelation(a.getUploader(), b.getUploader()));
//		percentages.add(getCreationTimeRelation(a.getCreationTime(), b.getCreationTime()));
//		percentages.add(getLocationRelation(a.getLocation(), b.getLocation()));
//		percentages.add(getLocationRelation(a.getLocation(), b.getLocation()));
//		percentages.add(getCommentsRelation(a.getComments(), b.getComments()));
//		percentages.add(getCommentRelation(a.getComments(), b.getComments()));



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
		if(a.isEmpty() && b.isEmpty()){
			return 0; // cause reasons
		}
		intersection.retainAll(b);
		return ((double) intersection.size())/(2 + Math.max(a.size(),b.size()));
	}

	private double getCommentRelation(List<String> a, List<String> b){
		List<String> aTags = new ArrayList<>();
		List<String> bTags = new ArrayList<>();

		for (String s : a) {
			aTags.addAll(hashtags(s));
		}
		for (String s : b) {
			bTags.addAll(hashtags(s));
		}
		return getTagRelation(aTags, bTags);
	}

	private List<String> hashtags(String comment) {
		Matcher m = hashtagPattern.matcher(comment);
		List<String> hashtags = new ArrayList<>();
		while (m.find()) {
			hashtags.add(m.group());
		}
		return hashtags;
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
