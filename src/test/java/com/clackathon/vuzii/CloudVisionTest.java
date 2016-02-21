package com.clackathon.vuzii;

import com.clackathon.vuzii.api.google.CloudVision;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;

/**
 * @author Ken Li (iliawnek)
 */

public class CloudVisionTest {
	@SneakyThrows
	public void testImages(String resourceFolder) {
		File[] imageFiles = getTestImages(resourceFolder);
		for (File image : imageFiles) {
			System.out.println(
				image.getName() +
				CloudVision.INSTANCE.getLabels(Image.imageDataOf(image.toPath()))
			);
		}
	}

	@Test
	public void testAllImages() {
		testImages("google");
	}

	private File[] getTestImages(String resourceFolder) {
		return new File("./src/test/resources/" + resourceFolder + "/").listFiles();
	}
}
