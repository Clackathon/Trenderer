package com.clackathon.vuzii;

import com.clackathon.vuzii.api.google.CloudVision;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

/**
 * @author Ken Li (iliawnek)
 */

public class CloudVisionTest {
	@SneakyThrows
	public void testImages(String resourceFolder) {
		File[] imageFiles = getTestImages(resourceFolder);
		val images = new ArrayList<Image.ImageData>();
		for (File image : imageFiles) {
			System.out.println(
				image.getName() +
				new CloudVision(Image.imageDataOf(image.toPath())).getLabels()
			);
		}
	}

	@Test
	public void testAllImages() {
		testImages("google");
		testImages("ryan");
	}

	private File[] getTestImages(String resourceFolder) {
		return new File("./src/test/resources/" + resourceFolder + "/").listFiles();
	}
}
