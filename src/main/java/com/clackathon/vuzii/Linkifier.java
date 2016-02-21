package com.clackathon.vuzii;

import lombok.val;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Euan
 */
public class Linkifier {
	public final List<Image> images;
	public final Map<Long, Image> imagesById = new HashMap<>();
	public final Relation relation;
	private static final int MAX_LINKS = 8;

	public Linkifier(List<Image> images, Relation relation) {
		this.images = images;
		this.relation = relation;
		for (Image image : images) {
			imagesById.put(image.getMediaId(), image);
		}
	}

	public void linkify() {
		for (Image a : images) {
			for (Image b : images) {
				linkify(a, b);
			}
		}
		dumpStats();

		for (Image a : images) {
			cleanLinks(a);
		}
	}

	private void cleanLinks(Image a) {
		val links = a.getLinkifiedImages();

		int size = links.size();
		val iterator = links.keySet().iterator();
		while (size > MAX_LINKS && iterator.hasNext()) {
			long other = iterator.next();
			val otherImage = imagesById.get(other);
			if (otherImage.getLinkifiedImages().isEmpty())
				otherImage.getLinkifiedImages().put(a.getMediaId(), links.get(other));
			iterator.remove();
		}
	}

	private void linkify(Image a, Image b) {
		if (a == b)
			return;

		double relativity = relation.relativity(a, b);

		if (relativity < 0.25)
			return;
		// link with relatives

		addLink(a, b, relativity);
	}

	private void addLink(Image a, Image b, double relativity) {
		val aLinked = a.getLinkifiedImages();
		val bLinked = b.getLinkifiedImages();

		if (aLinked.containsKey(b.getMediaId()) || bLinked.containsKey(a.getMediaId()))
			return;

		aLinked.put(b.getMediaId(), relativity);
	}

	public void dumpStats() {
		int links = 0;
		for (Image a : images) {
			links += a.getLinkifiedImages().size();
		}
		System.out.println(images.size() + " images with " + links + " links between images");
	}
}
