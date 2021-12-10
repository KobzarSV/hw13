package ua.goit.hw13;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HttpUtil {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Gson GSON = new Gson();
    private static final String JSONPLACEHOLDER_URL = "https://jsonplaceholder.typicode.com/";

    public static User createUserPost(User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", JSONPLACEHOLDER_URL, "users")))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        final User userResult = GSON.fromJson(response.body(), User.class);
        return userResult;
    }

    public static User updateUserPut(int id, User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", JSONPLACEHOLDER_URL, "users", id)))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-type", "application/json")
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        final User userResult = GSON.fromJson(response.body(), User.class);
        return userResult;
    }

    public static int userDelete(User user) throws IOException, InterruptedException {
        final String requestBody = GSON.toJson(user);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", JSONPLACEHOLDER_URL, "users", user.getId())))
                .header("Content-type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }

    public static List<User> infoAllUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s", JSONPLACEHOLDER_URL, "users")))
                .GET()
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        final List<User> users = GSON.fromJson(response.body(), new TypeToken<List<User>>() {
        }.getType());
        return users;
    }

    public static User infoUserId(int id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d", JSONPLACEHOLDER_URL, "users", id)))
                .GET()
                .header("Content-type", "application/json")
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        final User userResult = GSON.fromJson(response.body(), User.class);
        return userResult;
    }

    public static List<User> infoUserName(String name) throws IOException, InterruptedException {
        final List<User> users = infoAllUsers();
        return users.stream().filter(user -> user.getName().equals(name)).collect(Collectors.toList());
    }

    public static void createdJsonFile(int userId) throws IOException, InterruptedException {
        Post lastPostUser = lastComentPostUser(userId);
        String fileGson = "src\\main\\resources\\user-" + userId + "-post-" + lastPostUser.getId() + "-comments.json";
        File file = new File(fileGson);
        checkIfFileAvailable(file);
        writeFilJson(fileGson, lastPostUser);
        System.out.println("File created: " + file.getPath());
    }

    public static Post lastComentPostUser(int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d/%s", JSONPLACEHOLDER_URL, "users", userId, "posts")))
                .GET()
                .header("Content-type", "application/json")
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        final List<Post> postsList = GSON.fromJson(response.body(), new TypeToken<List<Post>>() {
        }.getType());
        final Post post = Collections.max(postsList, Comparator.comparing(Post::getId));
        System.out.println(post);
        return post;
    }

    public static void checkIfFileAvailable(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void writeFilJson(String path, Post post) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(post, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static List<Todos> unCompleted(int userId) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s%s/%d/%s", JSONPLACEHOLDER_URL, "users", userId, "todos")))
                .GET()
                .build();
        final HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        final List<Todos> todosList = GSON.fromJson(response.body(), new TypeToken<List<Todos>>() {
        }.getType());
        final List<Todos> resultList = todosList.stream().filter(todo -> !todo.isCompleted()).collect(Collectors.toList());
        return resultList;
    }
}
