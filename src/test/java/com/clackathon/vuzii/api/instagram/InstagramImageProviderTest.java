package com.clackathon.vuzii.api.instagram;

import lombok.val;

public class InstagramImageProviderTest {
	/* Disabled: Instagram API sandboxed
	@Test*/
	public void testGetImages() throws Exception {
		val provider = new InstagramImageProvider();
		val images = provider.getImages();
		System.out.println(images);
	}
}
