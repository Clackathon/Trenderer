package com.clackathon.vuzii.api.flatfile;

import com.clackathon.vuzii.Image;
import com.clackathon.vuzii.Location;
import com.clackathon.vuzii.User;
import com.clackathon.vuzii.api.ImageProvider;
import com.clackathon.vuzii.api.google.CloudVision;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
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
		image.setUserTags(generateTags());
		image.setComments(generateComments());
		val name = generateName();
		image.setUploader(new User(name.hashCode(), name));
		System.out.println(image);
		return image;
	}

	private final String[] tags = new String[] {
		"chair", "sofa", "dining table", "fireplace", "cabinet", "bed", "chest of drawers", "light", "lamp", "banana", "bath", "shower", "sync", "cooker", "cupboard"
	};

	private List<String> generateTags() {
		List<String> tags = new ArrayList<>(Arrays.asList(this.tags));
		Collections.shuffle(tags);
		return tags.subList(0, (int) Math.floor(Math.random() * tags.size()));
	}

	private final String[] parts = new String[] {
		"bearded", "octo", "nemesis", "euan", "mason", "ken", "cameron", "ross", "ryan", "wat"
	};

	private String generateName() {
		Random r = new Random();
		return parts[r.nextInt(parts.length)] + '-' + parts[r.nextInt(parts.length)];
	}

	private final String[] comments = new String[] {
		"#lol", "#awesome", "#derp", "#wat", "#NaNNaNNaNNaNBatman"
	};

	private List<String> generateComments() {
		int count = new Random().nextInt(5);

		val list = new ArrayList<String>();
		while (count --> 0) {
			list.add(generateComment());
		}

		return list;
	}

	private String generateComment() {
		StringBuilder s = new StringBuilder();
		Random r = new Random();
		int len = r.nextInt(15);
		int i = 0;
		while (i++ < len)
			s.append(comments[r.nextInt(comments.length)]).append(" ");

		return s.toString().trim();
	}

	private Location generateLocation() {
		return new Location(Math.random(), Math.random());
	}
}
