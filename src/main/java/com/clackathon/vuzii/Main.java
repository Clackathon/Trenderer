package com.clackathon.vuzii;

import com.clackathon.vuzii.api.ImageProvider;
import com.clackathon.vuzii.api.flatfile.FlatFileImageProvider;

import java.util.List;

public class Main {
	public static void main(String[] args) {
		ImageProvider provider = new FlatFileImageProvider();
		List<Image> images = provider.getImages();
		new Linkifier(images, new Relation()).linkify();
	}
}
