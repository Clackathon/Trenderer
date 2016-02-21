package com.clackathon.vuzii;

import lombok.SneakyThrows;
import lombok.val;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Supplier;

public class ImageCache {
	public static ImageCache INSTANCE = new ImageCache();

	Path cacheDir;

	@SneakyThrows
	private ImageCache() {
		cacheDir = Paths.get("./cache/");
		Files.createDirectories(cacheDir);
	}

	@SneakyThrows
	public BufferedImage downloadImage(URL url, String uniqueID) {
		Path cachedImage = cacheDir.resolve(uniqueID + ".cached.png");

		if (!Files.exists(cachedImage)) {
			Files.copy(url.openStream(), cachedImage);
		}

		return ImageIO.read(cachedImage.toFile());
	}

	@SuppressWarnings("unchecked")
	@SneakyThrows
	public List<String> getTags(Supplier<List<String>> supplier, String uniqueID) {
		Path cachedData = cacheDir.resolve(uniqueID + ".cached.vision");

		List<String> result;
		if (!Files.exists(cachedData)) {
			result = supplier.get();
			try (val out = new ObjectOutputStream(new FileOutputStream(cachedData.toFile()))) {
				out.writeObject(result);
			}
		} else {
			try (val in = new ObjectInputStream(new FileInputStream(cachedData.toFile()))) {
				result = (List<String>) in.readObject();
			}
		}

		return result;
	}
}
