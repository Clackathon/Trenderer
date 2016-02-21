package com.clackathon.vuzii;

import com.clackathon.vuzii.api.ImageProvider;
import com.clackathon.vuzii.api.flatfile.FlatFileImageProvider;
import lombok.SneakyThrows;
import lombok.val;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.List;

public class Main {
	@SneakyThrows
	public static void main(String[] args) {
		ImageProvider provider = new FlatFileImageProvider();
		List<Image> images = provider.getImages();
		val linkifier = new Linkifier(images, new Relation());
		linkifier.linkify();
		linkifier.dumpStats();
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File("./linked.json"), linkifier.imagesById);
	}
}
