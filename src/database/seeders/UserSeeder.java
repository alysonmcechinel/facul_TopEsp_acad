package database.seeders;

import database.models.user.User;
import database.models.user.UserTypeEnum;
import database.service.Service;

import java.util.List;

public class UserSeeder implements Seeder {
    @Override
    public void run() {
        Service<User> userService = new Service<>(User.class);
        Long count = userService.count();

        if (count.intValue() > 0) {
            return;
        }

        System.out.println("[Seeder] UserSeeder running.");
        User userAdmin = new User();
        userAdmin.setUsername("admin");
        userAdmin.setName("Administrador");
        userAdmin.setPassword("admin");
        userAdmin.setType(UserTypeEnum.ADMINISTRADOR);
        userAdmin.save();

        User userCadastral = new User();
        userCadastral.setUsername("user_cadastral");
        userCadastral.setName("Cadastral");
        userCadastral.setPassword("cadastral");
        userCadastral.setType(UserTypeEnum.CADASTRAL);
        userCadastral.save();

        User userFinanceiro = new User();
        userFinanceiro.setName("user_financeiro");
        userFinanceiro.setPassword("financeiro");
        userFinanceiro.setType(UserTypeEnum.FINANCEIRO);
        userFinanceiro.save();

        User userProfessor = new User();
        userFinanceiro.setName("user_professor");
        userFinanceiro.setPassword("professor");
        userFinanceiro.setType(UserTypeEnum.PROFESSOR);
        userFinanceiro.save();
        System.out.println("[Seeder] UserSeeder runned.");
    }
}
