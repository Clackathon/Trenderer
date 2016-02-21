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

/**
 *
 *
 * @author Cameron
 */
@Data
public class Image {
	private String mediaId;
	private List<String> userTags;
	private List<String> cloudVisionTags;
	private List<User> likers;
	private List<String> comments;
	private ImageData standardResolution;
	private User uploader;
	private LocalDateTime creationTime;
	private Location location;
	private Set<String> linkifiedImages = new HashSet<>();

    @Data
	@AllArgsConstructor
    public static class ImageData {
		private String uniqueId;
		private URL imageLocation;
		private int width;
        private int height;

		public ImageData(String s, URL uri) {
			uniqueId = s;
			imageLocation = uri;
			BufferedImage downloaded = download();
			width = downloaded.getWidth();
			height = downloaded.getHeight();
		}

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
