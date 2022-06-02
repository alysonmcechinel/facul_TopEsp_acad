package application;


import database.models.user.User;

public class ApplicationContext {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ApplicationContext.user = user;
    }
}
