package com.clackathon.vuzii.api.google;

import com.clackathon.vuzii.Image.ImageData;
import com.clackathon.vuzii.ImageCache;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionScopes;
import com.google.api.services.vision.v1.model.*;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class CloudVision {
	public static final CloudVision INSTANCE = new CloudVision();
	private static final String APPLICATION_NAME = "Clackathon-Trendr/1.0";
	private static final int MAX_LABELS = 10;
	private static final float MINIMUM_SCORE = 0.6f;
	private final Vision vision;

	@SneakyThrows
	private CloudVision() {
		this.vision = getVisionService();
	}

	// Returns a list of labels of an image using the Vision API.
	// List depends on MAX_LABELS and MINIMUM_SCORE.
	public List<String> getLabels(ImageData image) throws IOException, GeneralSecurityException {
		return ImageCache.INSTANCE.getTags(() -> {
			List<EntityAnnotation> labels = null;
			try {
				labels = labelImage(image.download());
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			List<String> labelNames = new ArrayList<>();
			for (EntityAnnotation label : labels) {
				if (label.getScore() >= MINIMUM_SCORE) {
					labelNames.add(label.getDescription());
				}
			}
			return labelNames;
		}, image.getUniqueId());
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
	private List<EntityAnnotation> labelImage(BufferedImage image) throws IOException {
		// convert BufferedImage to Byte[]
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "jpg", baos);
		byte[] data = baos.toByteArray();

		AnnotateImageRequest request =
			new AnnotateImageRequest()
				.setImage(new Image().encodeContent(data))
				.setFeatures(ImmutableList.of(
					new Feature()
						.setType("LABEL_DETECTION")
						.setMaxResults(MAX_LABELS)));
		Vision.Images.Annotate annotate =
			vision.images()
				.annotate(new BatchAnnotateImagesRequest().setRequests(ImmutableList.of(request)));
		// Due to a bug: requests to Vision API containing large images fail when GZipped.
		annotate.setDisableGZipContent(true);

		BatchAnnotateImagesResponse response = annotate.execute();
		return response.getResponses().get(0).getLabelAnnotations();
	}
}
