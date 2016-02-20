package com.clackathon.vuzii.api.instagram;

import org.junit.Test;

public class InstagramImageProviderTest {

	@Test
	public void testGetImages() throws Exception {
		System.out.println(new InstagramImageProvider().getImages());
	}
}
