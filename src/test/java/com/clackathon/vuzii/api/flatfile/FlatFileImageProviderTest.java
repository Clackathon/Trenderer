package com.clackathon.vuzii.api.flatfile;

import org.junit.Test;

public class FlatFileImageProviderTest {
	@Test
	public void testGetImages() throws Exception {
		System.out.println(new FlatFileImageProvider().getImages());
	}
}
