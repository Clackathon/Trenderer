package com.clackathon.vuzii;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * Unit tests for {@link CloudVision}.
 */
@RunWith(JUnit4.class)
public class CloudVisionTest {
	private static final int MAX_LABELS = 3;

	@Test
	public void labelImage_cat_returnsCatDescription() throws Exception {
		// Arrange
		CloudVision appUnderTest = new CloudVision(CloudVision.getVisionService());

		// Act
		List<EntityAnnotation> labels =
			appUnderTest.labelImage(Paths.get("../../data/label/cat.jpg"), MAX_LABELS);

		// Assert
		ImmutableSet.Builder<String> builder = ImmutableSet.builder();
		for (EntityAnnotation label : labels) {
			builder.add(label.getDescription());
		}
		ImmutableSet<String> descriptions = builder.build();

		assertThat(descriptions).named("cat.jpg labels").contains("cat");
	}

	@Test
	public void labelImage_badImage_throwsException() throws Exception {
		CloudVision appUnderTest = new CloudVision(CloudVision.getVisionService());

		try {
			appUnderTest.labelImage(Paths.get("../../data/bad.txt"), MAX_LABELS);
			fail("Expected GoogleJsonResponseException");
		} catch (GoogleJsonResponseException expected) {
			assertThat(expected.getDetails().getCode())
				.named("GoogleJsonResponseException Error Code")
				.isEqualTo(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Test
	public void printLabels_emptyList_printsNoLabelsFound() throws Exception {
		// Arrange
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bout);

		// Act
		CloudVision.printLabels(
			out, Paths.get("path/to/some/image.jpg"), ImmutableList.<EntityAnnotation>of());

		// Assert
		assertThat(bout.toString()).contains("No labels found.");
	}

	@Test
	public void printLabels_manyLabels_printsLabels() throws Exception {
		// Arrange
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bout);
		ImmutableList<EntityAnnotation> labels =
			ImmutableList.of(
				new EntityAnnotation().setDescription("dog").setScore(0.7564f),
				new EntityAnnotation().setDescription("husky").setScore(0.67891f),
				new EntityAnnotation().setDescription("poodle").setScore(0.1233f));

		// Act
		CloudVision.printLabels(out, Paths.get("path/to/some/image.jpg"), labels);

		// Assert
		String got = bout.toString();
		assertThat(got).contains("dog (score: 0.756)");
		assertThat(got).contains("husky (score: 0.679)");
		assertThat(got).contains("poodle (score: 0.123)");
	}
}
