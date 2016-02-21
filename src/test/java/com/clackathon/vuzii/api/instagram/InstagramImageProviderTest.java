package com.clackathon.vuzii.api.instagram;

import lombok.val;
import org.junit.Test;

public class InstagramImageProviderTest {

	@Test
	public void testGetImages() throws Exception {
		val provider = new InstagramImageProvider();
		val images = provider.getImages();
		System.out.println(images);
	}
}
