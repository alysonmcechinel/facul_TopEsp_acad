package graphic.commons;

import database.models.Model;
import database.models.user.User;
import database.service.Service;
import graphic.user.UserForm;
import lib.Observable;
import lib.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ConsultaGenericaWindow<T extends Model<T>> extends JDialog implements Observable {
    private JScrollPane scroll;
    private JTable table;
    private JTextField fieldFilter;
    private DefaultTableModel model;

    private Service<T> service;
    private List<Observer> observers;

    private JButton buttonOk, buttonFilter;

    private List<T> allObjects;

    public static void main(String[] args) throws IOException {
        ConsultaGenericaWindow<User> consulta = new ConsultaGenericaWindow<>(User.class, new UserForm(), new String[]{"Nome", "Usuario", "Tipo"});
        consulta.open();
    }

    public ConsultaGenericaWindow(final Class<T> clazz, final Observer observer, final String[] colunas) {
        this.service = new Service<>(clazz);
        this.observers = new ArrayList<>();

        addOberver(observer);

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (int i = 0; i < colunas.length; i++) {
            model.addColumn(colunas[i]);
        }

        setTitle("Usuários");
        setSize(500, 400);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        fieldFilter = new JTextField();
        fieldFilter.setBounds(5, 5, 400, 30);

        buttonFilter = new JButton("Buscar");
        buttonFilter.setBounds(410, 5, 80, 30);
        buttonFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    clearRows();

                    Constructor<T> constructor = clazz.getDeclaredConstructor();
                    T instance = constructor.newInstance(null);

                    List<T> list = instance.filter(fieldFilter.getText());

                    for (T t : list) {
                        model.addRow(t.getResult());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        table = new JTable(model);
        scroll = new JScrollPane(table);
        scroll.setBounds(0, 40, 500, 260);

        buttonOk = new JButton("Ok");
        buttonOk.setBounds(0, 305, 100, 25);
        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyAllObervers();
            }
        });

        getContentPane().add(scroll);
        getContentPane().add(buttonOk);
        getContentPane().add(fieldFilter);
        getContentPane().add(buttonFilter);
    }

    private void buscarDados() {
        allObjects = this.service.findAll();

        if (allObjects.isEmpty()) {
            // Error
            return;
        }

        for (T t : allObjects) {
            model.addRow(t.getResult());
        }
    }

    public void open() {
        buscarDados();
        setVisible(true);
    }

    public void clearRows(){
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    @Override
    public void addOberver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeOberver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAllObervers() {
        for (Observer observer : observers) {
            int position = table.getSelectedRow();

            if (position >= 0) {
                observer.update(allObjects.get(position));
            }
        }

        setVisible(false);
    }
}

