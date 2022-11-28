package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

public class Main {

    private final static String welcome = "Добро пожаловать. Введите \"signUp {имя пользователя}\", если хотите " +
            "зарегистрироваться. Введите \"signIn {имя пользователя}\", если хотите войти в учётную запись. Введите " +
            "\"quit\", если хотите выйти из программы.";

    private final static String signUpSuccessful = "Регистрация прошла успешно.";

    private final static String signUpFailed = "Не удалось зарегистрировать нового пользователя, так как пользователь " +
            "с данным именем уже содержится в базе. Введите команду заново.";

    private final static String signInSuccessful = "Вы вошли в учётную запись.";

    private final static String signInFailed = "Не удалось войти, так как пользователь с данным именем не содержится в " +
            "базе. Введите команду заново.";

    private final static String incorrectCommand = "Некорректная команда. Введите корректную команду.";

    private final static String inputHelp = " Введите \"help\", чтобы вывести список команд.";

    private final static String signUpUrl = "http://localhost:80/sign_up";

    private final static String signInUrl = "http://localhost:80/sign_in/";

    private final static String newTaskUrl = "http://localhost:80/tasks";

    private final static String currentTasksUrl = "http://localhost:80/current_tasks/";

    private final static String statusHistoryUrl = "http://localhost:80/task_history/";

    private static final String commandList = """
            Список команд:
            newTask {taskName} - создание новой задачи
            tasksList - отображение списка созданных задач
            statusHistory {taskName} - отображение истории смены статусов задачи
            quit - завершить работу программы""";

    private static class TaskView {
        public String taskID;
        public String taskName;
        public String status;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(welcome);
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String command = r.readLine().trim();
        String[] signParts = command.split(" ");
        String username = null;
        while(username == null) {
            if (signParts.length == 2) {
                switch(signParts[0]) {
                    case "signUp" -> {
                        if(signUp(signParts[1]) == 200) {
                            System.out.println(signUpSuccessful + " " + signInSuccessful + inputHelp);
                            username = signParts[1];
                        } else System.out.println(signUpFailed);
                    }
                    case "signIn" -> {
                        if(signIn(signParts[1]) == 200) {
                            System.out.println(signInSuccessful + inputHelp);
                            username = signParts[1];
                        } else System.out.println(signInFailed);
                    }
                    default -> System.out.println(incorrectCommand);
                }
            } else if(signParts.length == 1 && signParts[0].equals("quit")) return;
            else System.out.println(incorrectCommand);
            if(username == null) {
                command = r.readLine().trim();
                signParts = command.split(" ");
            }
        }
        command = r.readLine().trim();
        while(!command.equals("quit")) {
            String[] commandParts = command.split(" ");
            if(commandParts.length < 1 || commandParts.length > 2) {
                System.out.println(incorrectCommand);
                command = r.readLine().trim();
                continue;
            }
            switch(commandParts[0]) {
                case "help" -> System.out.println(commandList);
                case "newTask" -> newTask(username, commandParts[1]);
                case "tasksList" -> currentTasks(username);
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

    private static int signUp(String username) throws Exception {
        return HttpClient.newHttpClient().send(buildHttpRequest(signUpUrl, "POST", "{"
                + usernameFormat(username) + "}"), HttpResponse.BodyHandlers.ofString()).statusCode();
    }

    private static int signIn(String username) throws Exception {
        return HttpClient.newHttpClient().send(buildHttpRequest(signInUrl + username, "GET", ""),
                HttpResponse.BodyHandlers.ofString()).statusCode();
    }

    private static void newTask(String username, String taskName) throws Exception {
        System.out.println(HttpClient.newHttpClient().send(buildHttpRequest(newTaskUrl, "POST", "{"
                        + usernameFormat(username) + ", " + taskNameFormat(taskName) + "}"),
                HttpResponse.BodyHandlers.ofString()).body());
    }

    private static void currentTasks(String username) throws Exception {
        String outputStr = HttpClient.newHttpClient().send(buildHttpRequest(currentTasksUrl + username,
                        "GET", ""), HttpResponse.BodyHandlers.ofString()).body();
        outputStr = outputStr.substring(16, outputStr.length() - 1);
        System.out.println("Список задач:\n");
        Arrays.stream(new ObjectMapper().readValue(outputStr, TaskView[].class)).forEach((x) ->
                System.out.println("task ID:\t" + x.taskID + ",\ntask name:\t" + x.taskName + ",\nstatus:\t"
                        + x.status +"\n"));
    }

    private static void statusHistory(String username, String taskName) throws Exception {
        System.out.println(HttpClient.newHttpClient().send(buildHttpRequest(statusHistoryUrl + username + "/"
                + taskName, "GET", ""), HttpResponse.BodyHandlers.ofString()).body());
    }

}