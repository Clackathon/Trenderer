package com.clackathon.vuzii.api.instagram;

import com.clackathon.vuzii.Image;
import com.clackathon.vuzii.api.ImageProvider;
import lombok.SneakyThrows;
import lombok.val;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Verifier;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class InstagramImageProvider implements ImageProvider {
	private Instagram instagram = getInstagramAuthService();
	final Properties apiProperties = loadProperties();

	@SneakyThrows
	private Properties loadProperties() {
		val p = new Properties();
		p.load(new FileInputStream("./secrets/instagram.conf"));
		return p;
	}

	private Instagram getInstagramAuthService() {
		val service =  new InstagramAuthService()
			.apiKey(apiProperties.getProperty("INSTAGRAM_KEY"))
			.apiSecret(apiProperties.getProperty("INSTAGRAM_SECRET"))
			.callback("http://localhost/")
			.build();

		String authorizationUrl = service.getAuthorizationUrl();

		System.out.println("Visit " + authorizationUrl);
		System.out.println("Enter verifier code: ");

		String verifierCode = new Scanner(System.in).next();

		val verifier = new Verifier(verifierCode);
		return new Instagram(service.getAccessToken(verifier));
	}

	@SneakyThrows
	public List<Image> getImages() {
		System.out.println(instagram.getUserRecentMedia());
		throw new UnsupportedOperationException("todo");
	}
}
