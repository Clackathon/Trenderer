package com.clackathon.vuzii;

import com.clackathon.vuzii.api.ImageProvider;
import com.clackathon.vuzii.api.flatfile.FlatFileImageProvider;
import lombok.val;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		ImageProvider provider = new FlatFileImageProvider();
		List<Image> images = provider.getImages();
		val linkifier = new Linkifier(images, new Relation());
		linkifier.linkify();
		linkifier.dumpStats();
	}
}
