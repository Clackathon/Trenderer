package com.clackathon.vuzii;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author Cameron
 */
@Data
public class Image {
	private long mediaId;
	private List<String> userTags;
	private List<String> cloudVisionTags;
	private List<User> likers;
	private List<String> comments;
	private ImageData standardResolution;
	private User uploader;
	private Date creationTime;
	private Location location;

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

	@SneakyThrows
	public static ImageData imageDataOf(Path p) {
		return new ImageData(p.getFileName().toString(), p.toUri().toURL());
	}
}
