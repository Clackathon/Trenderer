package com.clackathon.vuzii.api.instagram;

import com.clackathon.vuzii.Image;
import com.clackathon.vuzii.api.ImageProvider;
import lombok.SneakyThrows;
import lombok.val;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class InstagramImageProvider implements ImageProvider {
	final Properties apiProperties = loadProperties();
	private Instagram instagram = getInstagramAuthService();
	private static String instagramConf = "./secrets/instagram.conf";

	@SneakyThrows
	private Properties loadProperties() {
		val p = new Properties();
		p.load(new FileInputStream(instagramConf));
		return p;
	}

	@SneakyThrows
	private Instagram getInstagramAuthService() {
		val service =  new InstagramAuthService()
			.apiKey(apiProperties.getProperty("INSTAGRAM_KEY"))
			.apiSecret(apiProperties.getProperty("INSTAGRAM_SECRET"))
			.callback("http://localhost/")
			.scope("basic public_content follower_list comments relationships like")
			.build();

		String tokenSecret = apiProperties.getProperty("INSTAGRAM_TOKEN_SECRET");
		String tokenToken = apiProperties.getProperty("INSTAGRAM_TOKEN_TOKEN");
		Token token;

		if (tokenSecret != null && tokenToken != null) {
			token = new Token(tokenToken, tokenSecret);
		} else {
			String authorizationUrl = service.getAuthorizationUrl();

			System.out.println("Visit " + authorizationUrl);
			System.out.println("Enter verifier code: ");

			String verifierCode = new Scanner(System.in).next();

			val verifier = new Verifier(verifierCode);
			token = service.getAccessToken(verifier);

			apiProperties.put("INSTAGRAM_TOKEN_SECRET", token.getSecret());
			apiProperties.put("INSTAGRAM_TOKEN_TOKEN", token.getToken());
			apiProperties.store(new FileOutputStream(instagramConf), "added token");
		}

		return new Instagram(token);
	}

	/**
	 * Sets up verifier token for first use
	 */
	public static void main(String[] args) {
		System.out.println(new InstagramImageProvider().getInstagramAuthService().getAccessToken());
	}

	@SneakyThrows
	public List<Image> getImages() {
		val mediaFeed = instagram.getUserRecentMedia(100, null, null);
		System.out.println(mediaFeed.getData());
		throw new UnsupportedOperationException("todo");
	}
}
