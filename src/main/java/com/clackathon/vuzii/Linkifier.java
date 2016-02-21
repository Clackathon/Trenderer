package com.clackathon.vuzii;

import lombok.RequiredArgsConstructor;

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
		a.getLinkifiedImages().add(b.getMediaId());
		b.getLinkifiedImages().add(a.getMediaId());

	}

	public void dumpStats() {
		int links = 0;
		for (Image a : images) {
			links += a.getLinkifiedImages().size();
		}
		System.out.println(images.size() + " images with " + links + " links between images");
	}
}
