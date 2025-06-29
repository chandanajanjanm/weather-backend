
package com.weather.weatherapp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")

public class WeatherController {

    private final String apiKey = "6ff3dfea0074132f991db4bbc03ebb09";
    private final Path userFile = Paths.get("users.txt");

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                     "&appid=" + apiKey + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/forecast")
    public String getForecast(@RequestParam String city) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city +
                     "&appid=" + apiKey + "&units=metric";
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> user) throws IOException {
        String username = user.get("username");
        String password = user.get("password");

        if (Files.exists(userFile)) {
            List<String> lines = Files.readAllLines(userFile);
            for (String line : lines) {
                if (line.split(":")[0].equals(username)) {
                    return "❌ Username already exists";
                }
            }
        }

        Files.write(userFile, Arrays.asList(username + ":" + password), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        return "✅ Registered successfully";
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> user) throws IOException {
        String username = user.get("username");
        String password = user.get("password");

        if (!Files.exists(userFile)) return "❌ No users registered";

        List<String> lines = Files.readAllLines(userFile);
        for (String line : lines) {
            String[] parts = line.split(":");
            if (parts[0].equals(username) && parts[1].equals(password)) {
                return "✅ Login successful";
            }
        }
        return "❌ Invalid credentials";
    }
}
