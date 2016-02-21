package com.clackathon.vuzii.api.flatfile;

import com.clackathon.vuzii.Image;
import com.clackathon.vuzii.Location;
import com.clackathon.vuzii.api.ImageProvider;
import com.clackathon.vuzii.api.google.CloudVision;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class FlatFileImageProvider implements ImageProvider {
	File images = new File("./src/test/resources/");

	@SneakyThrows
	@Override
	public List<Image> getImages() {
		return Files.walk(images.toPath())
			.filter(Files::isRegularFile)
			.map(this::imageFromFile).collect(Collectors.toList());
	}

	@SneakyThrows
	private Image imageFromFile(Path p) {
		val image = new Image();
		image.setLocation(generateLocation());
		image.setCreationTime(LocalDateTime.now());
		image.setMediaId(p.getFileName().toString());
		image.setStandardResolution(new Image.ImageData(p.getFileName().toString(), p.toFile().toURL()));
		image.setCloudVisionTags(new CloudVision(image.getStandardResolution()).getLabels());
		return image;
	}

	private Location generateLocation() {
		return new Location(Math.random(), Math.random());
	}
}
