package com.clackathon.vuzii;

import lombok.Data;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
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
	private long mediaID;
	private List<String> tags;
	private List<User> likers;
	private List<String> comments;
	private ImageData standard;
	private User uploader;
	private Date creationTime;
	private String location;

    @Data
    public static class ImageData {
		private URL imageLocation;
		private int width;
        private int height;

		@SneakyThrows
		public BufferedImage download() {
			return ImageIO.read(imageLocation);
		}
    }
}
