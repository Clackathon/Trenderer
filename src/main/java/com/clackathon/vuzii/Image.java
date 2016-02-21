package com.clackathon.vuzii;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
	private ImageData standardResolution;
	private User uploader;
	private LocalDateTime creationTime;
	private Location location;
	private Set<Long> linkifiedImages = new HashSet<>();

    @Data
	@AllArgsConstructor
    public static class ImageData {
		private String uniqueId;
		private URL imageLocation;

		@SneakyThrows
		public BufferedImage download() {
			return ImageCache.INSTANCE.downloadImage(imageLocation, uniqueId);
		}
    }

	// Used for CloudVisionTest.
	@SneakyThrows
	public static ImageData imageDataOf(Path p) {
		return new ImageData(p.getFileName().toString(), p.toUri().toURL());
	}
}
