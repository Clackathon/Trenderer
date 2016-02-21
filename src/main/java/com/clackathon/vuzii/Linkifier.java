package com.clackathon.vuzii;

import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.List;

/**
 * @author Euan
 */
@RequiredArgsConstructor
public class Linkifier {
	public final List<Image> images;
	public final Relation relation;

	public void linkify() {
		for (Image a : images) {
			for (Image b : images) {
				linkify(a, b);
			}
		}
	}

	private void linkify(Image a, Image b) {
		if (a == b)
			return;

		double relativity = relation.relativity(a, b);

		if (relativity < 0.19)
			return;
		// link with relatives

		addLink(a, b);
	}

	private void addLink(Image a, Image b) {
		val aLinked = a.getLinkifiedImages();
		val bLinked = b.getLinkifiedImages();

		if (aLinked.contains(b.getMediaId()) || bLinked.contains(a.getMediaId()))
			return;

		aLinked.add(b.getMediaId());
	}

	public void dumpStats() {
		int links = 0;
		for (Image a : images) {
			links += a.getLinkifiedImages().size();
		}
		System.out.println(images.size() + " images with " + links + " links between images");
	}
}
