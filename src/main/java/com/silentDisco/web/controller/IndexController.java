package com.silentDisco.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.silentDisco.data.Observer;

@Controller
public class IndexController {
	private RestTemplate gopher = new RestTemplate();// TODO make bean
	private List<Observer> animals = new ArrayList<>(); // TODO add observer code and ext here for now
	private Random number_generator = new Random();
	@Value("${spotify-client-id}")
	private String clientId;
	
	@RequestMapping("/*")
	public String home(Model model) {
		model.addAttribute("active_view", 0);
		return "index";
	}
	@RequestMapping("/host")
	public String hostView(Model model, HttpServletRequest request) {
		model.addAttribute("active_view", 1);
		//viewAsHost();
		System.out.println(request.getRequestURL());
//		System.out.println(request.);
		return "index";
	}
	@RequestMapping("/animal")
	public String animalView(Model model) {
		model.addAttribute("active_view", 2);		
		return "index";
	}
	@RequestMapping("/link")
	public String linkSpotifyAccount(Model model, @RequestParam String view, HttpServletRequest request) {
		String current_url = request.getRequestURL().toString(); // TODO
//		model.addAttribute("linked", 1);
//		switch (view) {
//			case "host": model.addAttribute("active_view", 1); break;
//			case "animal": model.addAttribute("active_view", 2); break;
//		}
		current_url = current_url.replace("link", view);
		
//		String response = gopher.getForObject(getAuthorization(current_url), String.class);
////		System.out.println(response);
//		JsonParser springParser = JsonParserFactory.getJsonParser();
//		Map<String, Object> map = springParser.parseMap(response);
//
//		String mapArray[] = new String[map.size()];
//		System.out.println("Items found: " + mapArray.length);
//
//		int i = 0;
//		for (Map.Entry<String, Object> entry : map.entrySet()) {
//				System.out.println(entry.getKey() + " = " + entry.getValue());
//				i++;
//		}
//		return "index";
		return "redirect:" + getAuthorization(current_url);
	}
	
	// Not mapping below
	private void addObserver(Observer animal) {
		animals.add(animal);
	}
	private void removeObserver(Observer animal) {
		animals.remove(animal);
	}
//	private void updateAll(Action action) {
//		for (Observer animal: animals) {
//			animal.update(action);
//		}
//	}
	
	private String getAuthorization(String return_to) {
		String authorize_url = "https://accounts.spotify.com/authorize";
		String identity_check = "", type = "token"; // TODO
		StringBuilder scopes = new StringBuilder();
		
		identity_check = String.valueOf(number_generator.nextInt());
		// all scopes ... for now
		// Playlists
		scopes.append("playlist-read-collaborative ");
		scopes.append("playlist-modify-private ");
		scopes.append("playlist-modify-public ");
		scopes.append("playlist-read-private ");
		// Spotify Connect
		scopes.append("user-modify-playback-state ");
		scopes.append("user-read-currently-playing ");
		scopes.append("user-read-playback-state ");
		// Users
		scopes.append("user-read-private ");
		scopes.append("user-read-email ");
		// Library
		scopes.append("user-library-modify ");
		scopes.append("user-library-read ");
		// Follow
		scopes.append("user-follow-modify ");
		scopes.append("user-follow-read ");
		// Listening History
		scopes.append("user-read-recently-played ");
		scopes.append("user-top-read ");
		// Playback
		scopes.append("streaming ");
		scopes.append("app-remote-control ");
		scopes.setLength(scopes.length() - 1); // last space
		
		UriComponents uri_destination = UriComponentsBuilder.fromHttpUrl(authorize_url)
				.query("client_id={keyword}")
				.query("redirect_uri={keyword}")
				.query("scope={keyword}")
				.query("response_type={keyword}")
				.query("state={keyword}")
				.buildAndExpand(
						clientId,
						return_to,
						scopes,
						"token",
						identity_check)
				.encode();
		
		return uri_destination.toUri().toString();
	}
}
