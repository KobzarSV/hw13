package ua.goit.hw13;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Task1/1");
        User user1 = new User();
        user1.setId(1);
        user1.setName("Sergey");
        user1.setUsername("SV");
        user1.setEmail("kobzars@gmail.com");
        user1.setPhone("+38068-999-11-99");

        User userCreate = HttpUtil.createUserPost(user1);
        System.out.println(userCreate);

        System.out.println("Task1/2");
        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setName("Ivan");
        userUpdate.setUsername("IV");
        userUpdate.setEmail("ivan@gmail.com");
        userUpdate.setPhone("+3067-111-99-11");
        User userUpdateId = HttpUtil.updateUserPut(1, userUpdate);
        System.out.println(userUpdateId);

        System.out.println("Task1/3");
        System.out.println(HttpUtil.userDelete(userUpdateId));

        System.out.println("Task1/4");
        final List<User> users = HttpUtil.infoAllUsers();
        System.out.println(users);

        System.out.println("Task1/5");
        System.out.println(HttpUtil.infoUserId(2));

        System.out.println("Task1/6");
        final List<User> user = HttpUtil.infoUserName("Ervin Howell");
        System.out.println(user);

        System.out.println("Task2");
        HttpUtil.createdJsonFile(3);

        System.out.println("Task3");
        System.out.println(HttpUtil.unCompleted(4));
    }
}
