package graphic.authentication;

import application.ApplicationContext;
import database.models.user.User;
import graphic.main.MenuWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Login extends JDialog {

    private JLabel lblusuario, lblsenha;
    private JTextField txtusuario;
    private JPasswordField psfsenha;
    private JButton btnentrar, btnsair;
    private JTable tbldados;
    private DefaultTableModel model;
    private String usuario;
    private String senha;
    private Boolean opened = false;

    public Login() {
        setTitle("Login - Academia");
        setSize(300, 160);
        setLayout(null);
        setResizable(false);
        setModal(true); //para nao deixar clicar no sistema
        setLocationRelativeTo(null); //para abrir no meio
        ComponentesCriar();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int retorno = JOptionPane.showConfirmDialog(null, "Deseja realmente sair?", "Informa��o", JOptionPane.YES_NO_OPTION);
                if (retorno == JOptionPane.YES_OPTION) {
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });
    }

    private void ComponentesCriar() {
        lblusuario = new JLabel("Usuario: ");
        lblusuario.setBounds(10, 10, 50, 25); //largura sempre 25
        getContentPane().add(lblusuario);

        txtusuario = new JTextField();
        txtusuario.setBounds(67, 10, 200, 25);
        getContentPane().add(txtusuario);

        lblsenha = new JLabel("Senha: ");
        lblsenha.setBounds(10, 40, 70, 25);
        getContentPane().add(lblsenha);

        psfsenha = new JPasswordField();
        psfsenha.setBounds(67, 40, 200, 25);
        getContentPane().add(psfsenha);

        btnentrar = new JButton(actionbtnEntrar);
        btnentrar.setBounds(50, 80, 100, 25);
        getContentPane().add(btnentrar);

        btnsair = new JButton(actionbtnSair);
        btnsair.setBounds(160, 80, 100, 25);
        getContentPane().add(btnsair);

    }

//--A��o do Bot�o Entrar

    Action actionbtnEntrar = new AbstractAction("Entrar") {

        @Override
        public void actionPerformed(ActionEvent e) {

            usuario = txtusuario.getText();
            senha = new String(psfsenha.getPassword());


            if (usuario.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Campo usuario obrigatorio!");
                return;
            }

            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Campo senha obrigatorio!");
                return;
            }

            try {
                User user = User.login(usuario, senha);
                if (user == null) {
                    throw new Exception("Erro ao fazer login.");
                }
                ApplicationContext.setUser(user);
                toogle();
                new MenuWindow().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Usuario ou senha invalido!");
                ex.printStackTrace();
            }
        }
    };

    //--A��o do Bot�o Sair

    Action actionbtnSair = new AbstractAction("Sair") {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(EXIT_ON_CLOSE);
        }
    };

    public Login toogle() {
        opened = !opened;
        setVisible(opened);
        return this;
    }

    public static void main(String[] args) {
        new Login().setVisible(true);
    }

}


