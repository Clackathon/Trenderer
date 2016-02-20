package com.clackathon.vuzii;

import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}
