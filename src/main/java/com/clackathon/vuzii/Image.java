package com.clackathon.vuzii;

import lombok.Data;
import lombok.SneakyThrows;

import java.awt.image.BufferedImage;
import java.net.URL;
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
    public class ImageData {
		private URL imageLocation;
		private int width;
        private int height;

		@SneakyThrows
		public BufferedImage download() {
			return ImageCache.INSTANCE.downloadImage(imageLocation, String.valueOf(getMediaId()));
		}
    }
}
