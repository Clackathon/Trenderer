package com.clackathon.vuzii;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 *
 * @author Cameron
 */
@Data
public class Image {
	private static final AtomicLong mediaIdCount = new AtomicLong();
	private long mediaId = mediaIdCount.incrementAndGet();
	private String label;
	private List<String> userTags;
	private List<String> cloudVisionTags;
	private List<User> likers;
	private List<String> comments;
	private String encodedThumbnail;
	private ImageData standardResolution;
	private User uploader;
	private LocalDateTime creationTime;
	private Location location;
	private Map<Long, Double> linkifiedImages = new TreeMap<>();

	private static final int THUMBNAIL_DIMENSION = 256;

	public void setStandardResolution(ImageData data) {
		standardResolution = data;
		if (encodedThumbnail == null)
			encodedThumbnail = Base64.getEncoder().encodeToString(imageBytes(data.createThumbnail()));
	}

	@SneakyThrows
	private static byte[] imageBytes(BufferedImage image) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "jpg", baos);
			return baos.toByteArray();
		}
	}

    @Data
	@AllArgsConstructor
    public static class ImageData {
		private String uniqueId;
		private URL imageLocation;

		@SneakyThrows
		public BufferedImage download() {
			return ImageCache.INSTANCE.downloadImage(imageLocation, uniqueId);
		}

		public BufferedImage createThumbnail() {
			val standard = download();
			int height = standard.getHeight();
			int width = standard.getWidth();
			float hScale = height / 256;
			float wScale = width / 256;
			float scale = Math.max(hScale, wScale);
			return toBufferedImage(standard.getScaledInstance((int) (height / scale), (int) (width / scale), BufferedImage.SCALE_SMOOTH));
		}

		private static BufferedImage toBufferedImage(java.awt.Image img) {
			if (img instanceof BufferedImage)
				return (BufferedImage) img;

			// Create a buffered image without transparency (must not use transparency for JPEGs)
			BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

			// Draw the image on to the buffered image
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(img, 0, 0, null);
			bGr.dispose();

			// Return the buffered image
			return bimage;
		}
    }

	// Used for CloudVisionTest.
	@SneakyThrows
	public static ImageData imageDataOf(Path p) {
		return new ImageData(p.getFileName().toString(), p.toUri().toURL());
	}
}
