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
	long mediaID;
	List<String> tags;
	List<User> likers;
	List<String> comments;
	ImageData standard;
	User uploader;
	Date creationTime;
	String location;


    @Data
    public static class ImageData {
		URL imageLocation;
		int width;
        int height;

		@SneakyThrows
		public BufferedImage download() {
			return ImageIO.read(imageLocation);
		}
    }
}
