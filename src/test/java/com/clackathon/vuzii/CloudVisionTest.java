package com.clackathon.vuzii;

import lombok.SneakyThrows;
import org.junit.Test;

/**
 * @author Ken Li (iliawnek)
 */

public class CloudVisionTest {
	@SneakyThrows
	@Test
	public void testOnTestImages() {
		CloudVision.run("bathroom.jpg");
	}
}
