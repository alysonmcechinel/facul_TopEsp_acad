import database.connection.HibernateUtil;
import graphic.authentication.Login;

public class Main {

    public static void main(final String[] args) {
        HibernateUtil.init();
        HibernateUtil.runSeeders();

        Login loginWindow = new Login();
        loginWindow.toogle();
    }

}
