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

		if (relativity < 0.7)
			return;
		// link with relatives
		a.getLinkifiedImages().add(b);
		b.getLinkifiedImages().add(a);

	}
}
