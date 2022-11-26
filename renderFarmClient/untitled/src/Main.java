import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    private final static String welcome = "Добро пожаловать. Введите \"signUp {имя пользователя}\", если хотите " +
            "зарегистрироваться. Введите \"signIn {имя пользователя}\", если хотите войти.";

    private final static String signUpSuccessful = "Регистрация прошла успешно.";

    private final static String signUpFailed = "Не удалось зарегистрировать нового пользователя, так как пользователь " +
            "с данным именем уже содержится в базе. Введите другое имя пользователя.";

    private final static String signInSuccessful = "Вы вошли.";

    private final static String signInFailed = "Не удалось войти, так как пользователь с данным именем не содержится " +
            "в базе. Введите другое имя пользователя.";

    private final static String incorrectCommand = "Некорректная команда. Введите корректную команду.";

    private final static String inputHelp = " Введите \"help\", чтобы вывести список команд.";

    private final static String signUpUrl = "http://localhost:8080/sign_up";

    private final static String signInUrl = "http://localhost:8080/sign_in/";

    private final static String newTaskUrl = "http://localhost:8080/tasks";

    private final static String currentTasksUrl = "http://localhost:8080/current_tasks/";

    private final static String statusHistoryUrl = "http://localhost:8080/task_history/";

    private static final String commandList = """
            Список команд:
            newTask {taskName} - создание новой задачи
            currentTasks - отображение списка созданных задач
            statusHistory {taskName} - отображение истории смены статусов задачи""";

    public static void main(String[] args) throws Exception {
        System.out.println(welcome);
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String command = r.readLine().trim();
        String[] signParts = command.split(" ");
        while(!((signParts[0].equals("signUp") || signParts[0].equals("signIn")) && signParts.length == 2)){
            System.out.println(incorrectCommand);
            command = r.readLine().trim();
            signParts = command.split(" ");
        }
        String username;
        username = signParts[1];
        if(signParts[0].equals("signUp")) {
            while(!signUp(username).equals(signUpSuccessful)) {
                System.out.println(signUpFailed);
                username = r.readLine().trim();
            }
            System.out.println(signUpSuccessful + " " + signInSuccessful + inputHelp);
        } else {
            while(!signIn(username).equals(signInSuccessful)) {
                System.out.println(signInFailed);
                username = r.readLine().trim();
            }
            System.out.println(signInSuccessful + inputHelp);
        }
        command = r.readLine().trim();
        while(!command.equals("quit")) {
            String[] commandParts = command.split(" ");
            if(commandParts.length < 1 || commandParts.length > 2) {
                System.out.println(incorrectCommand);
                command = r.readLine();
                continue;
            }
            switch(commandParts[0]) {
                case "help" -> System.out.println(commandList);
                case "newTask" -> newTask(username, commandParts[1]);
                case "currentTasks" -> currentTasks(username);
                case "statusHistory" -> statusHistory(username, commandParts[1]);
                default -> System.out.println(incorrectCommand);
            }
            command = r.readLine().trim();
        }
    }

    private static HttpRequest buildHttpRequest(String URL, String requestType, String requestBody) throws URISyntaxException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(new URI(URL))
                .headers("Content-Type", "application/json");
        return switch(requestType) {
            case "GET" -> requestBuilder.GET().build();
            case "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            default -> HttpRequest.newBuilder().build();
        };
    }

    private static String usernameFormat(String username) {
        return "\"username\": \"" + username + "\"";
    }

    private static String taskNameFormat(String taskName) {
        return "\"task_name\": \"" + taskName + "\"";
    }

    private static String signUp(String username) throws Exception {
        return HttpClient.newHttpClient().send(buildHttpRequest(signUpUrl, "POST",
                "{" + usernameFormat(username) + "}"), HttpResponse.BodyHandlers.ofString()).body();
    }

    private static String signIn(String username) throws Exception {
        return HttpClient.newHttpClient().send(buildHttpRequest(signInUrl + username, "GET", ""),
                HttpResponse.BodyHandlers.ofString()).body();
    }

    private static void newTask(String username, String taskName) throws Exception {
        System.out.println(HttpClient.newHttpClient().send(buildHttpRequest(newTaskUrl, "POST",
                "{" + usernameFormat(username) + ", " + taskNameFormat(taskName) + "}"),
                HttpResponse.BodyHandlers.ofString()).body());
    }

    private static void currentTasks(String username) throws Exception {
        String outputStr =  HttpClient.newHttpClient().send(buildHttpRequest(currentTasksUrl + username,
                "GET", ""), HttpResponse.BodyHandlers.ofString()).body()
                .replace(",", ",\n").replace("{\"Список задач\":", "Список задач:\n")
                .replace(":", ": ");
        System.out.println(outputStr.substring(0, outputStr.length() - 1));
    }

    private static void statusHistory(String username, String taskName) throws Exception {
        System.out.println(HttpClient.newHttpClient().send(buildHttpRequest(statusHistoryUrl + username + "/"
            + taskName, "GET", ""), HttpResponse.BodyHandlers.ofString()).body());
    }

}