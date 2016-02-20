package com.clackathon.vuzii;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;
import com.clackathon.vuzii.Image.ImageData;
import lombok.SneakyThrows;

@SuppressWarnings("serial")
public class CloudVision {

	private static final String APPLICATION_NAME = "Clackathon-Trendr/1.0";
	private static final int MAX_LABELS = 10;
	private final Vision vision;
	private ImageData image;

	@SneakyThrows
	public CloudVision(ImageData image) {
		this.image = image;
		this.vision = getVisionService();
	}

	// Returns a list of labels of an image using the Vision API.
	// Maximum: 10 labels.
	// Minimum score: 0.6.
	public List<String> getLabels(ImageData image) throws IOException, GeneralSecurityException {
//		Path imagePath = Paths.get("./src/test/resources/" + filename);
//		printLabels(System.out, imagePath, labelImage(imagePath, MAX_LABELS));
		List<EntityAnnotation>
	}

	// Prints the labels received from the Vision API.
	private void printLabels(PrintStream out, Path imagePath, List<EntityAnnotation> labels) {
		out.printf("Labels for image %s:\n", imagePath);
		for (EntityAnnotation label : labels) {
			out.printf(
				"\t%s (score: %.3f)\n",
				label.getDescription(),
				label.getScore());
		}
		if (labels.isEmpty()) {
			out.println("\tNo labels found.");
		}
	}

	// Connects to the Vision API using Application Default Credentials.
	private Vision getVisionService() throws IOException, GeneralSecurityException {
		GoogleCredential credential =
			GoogleCredential.getApplicationDefault().createScoped(VisionScopes.all());
		JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
		return new Vision.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
			.setApplicationName(APPLICATION_NAME)
			.build();
	}

	// Gets up to {@code maxResults} labels for an image stored at {@code path}.
	private List<EntityAnnotation> labelImage(Path path, int maxResults) throws IOException {
		byte[] data = Files.readAllBytes(path);

		AnnotateImageRequest request =
			new AnnotateImageRequest()
				.setImage(new Image().encodeContent(data))
				.setFeatures(ImmutableList.of(
					new Feature()
						.setType("LABEL_DETECTION")
						.setMaxResults(maxResults)));
		Vision.Images.Annotate annotate =
			vision.images()
				.annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
		// Due to a bug: requests to Vision API containing large images fail when GZipped.
		annotate.setDisableGZipContent(true);

		BatchAnnotateImagesResponse response = annotate.execute();
		return response.getResponses().get(0).getLabelAnnotations();
	}
}
