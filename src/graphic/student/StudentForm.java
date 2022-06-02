package graphic.student;

import database.models.address.Address;
import database.models.address.City;
import database.models.address.State;
import database.models.modality.Modality;
import database.models.modality.Student;
import database.service.CityService;
import database.service.Service;
import graphic.commons.ConsultaGenericaWindow;
import lib.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentForm extends JDialog implements Observer<Student> {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldName;
    private JTextField fieldTelephone;
    private JComboBox comboState;
    private JComboBox comboCity;
    private JTextField fieldNeighorhood;
    private JTextField fieldStreet;
    private JTable tblModalities;
    private JTextField fieldBirthDate;
    private JButton btnConsultar;
    private JButton btnDeletar;
    private JButton consultarModalidadesButton;

    private Student student = new Student();
    private List<State> states;
    private List<City> cities;
    private List<Modality> selectedModalities;
    private DefaultTableModel tblModel;

    public StudentForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(500, 500);
        setLocationRelativeTo(null);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        setContents();

        comboState.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = comboState.getSelectedIndex();

                if (selectedIndex == 0) {
                    return;
                }

                comboCity.removeAllItems();
                loadCities(states.get(selectedIndex - 1));
            }
        });

        tblModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblModel.addColumn("Nome");
        tblModel.addColumn("Valor");
        tblModalities.setModel(tblModel);

        StudentForm instance = this;
        btnConsultar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConsultaGenericaWindow<Student>(Student.class, instance, new String[]{"Nome", "Telefone", "Ativo"}).open();
            }
        });

        btnDeletar.setEnabled(Boolean.FALSE);
        btnDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (student != null && student.getId() != null) {
                    student.delete();
                    JOptionPane.showMessageDialog(null, "Aluno deletado com sucesso!");
                    student = null;
                    btnDeletar.setEnabled(Boolean.FALSE);
                    clear();
                }
            }
        });

        consultarModalidadesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentModalitiesDialog
                        .open(modalities -> {
                            selectedModalities = modalities;
                            addModalitiesInTable(modalities);
                            return null;
                        });
            }
        });
    }

    private void setContents() {
        Service<State> stateService = new Service<>(State.class);
        states = stateService.findAll();

        comboCity.addItem("Selecione uma cidade.");
        comboState.addItem("Selecione um estado");

        for (State state : states) {
            comboState.addItem(state.getName());
        }
    }

    private void loadCities(State state) {
        CityService cityService = new CityService();
        cities = cityService.getCitiesFromState(state);

        comboCity.addItem("Selecione uma cidade.");

        for (City city : cities) {
            comboCity.addItem(city.getName());
        }
    }

    private void onOK() {
        Address address = new Address();

        String name = fieldName.getText();
        String telephone = fieldTelephone.getText();
        String birthDate = fieldBirthDate.getText();
        String street = fieldStreet.getText();
        String neighorhood = fieldNeighorhood.getText();
        int citySelectedIndex = comboCity.getSelectedIndex();
        int stateSelectedIndex = comboState.getSelectedIndex();

        student.setAddress(address);

        if (stateSelectedIndex > 0) {
            State state = states.get(stateSelectedIndex - 1);
            address.setState(state);
        }

        if (citySelectedIndex > 0) {
            City city = cities.get(citySelectedIndex - 1);
            address.setCity(city);
        }

        if (selectedModalities != null) {
            List<Modality> modalities = student.getModalities();
            modalities.addAll(selectedModalities);
        }

        student.setName(name);
        student.setTelephone(telephone);
        student.setBirthDate(birthDate != null ? LocalDate.parse(birthDate) : null);
        address.setStreet(street);
        address.setNeighborhood(neighorhood);

        student.save();

        student = null;

        clear();

        JOptionPane.showMessageDialog(null, "Aluno salvo com sucesso!");
        //dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void deleteRowsTableModalities() {
        for (int i = tblModel.getRowCount() - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
    }

    public static void main(String[] args) {
        StudentForm dialog = new StudentForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    @Override
    public void update(Student student) {
        setStudent(student);
        deleteRowsTableModalities();
        addModalitiesInTable(student.getModalities());
        btnDeletar.setEnabled(Boolean.TRUE);

        Address address = student.getAddress();

        fieldName.setText(student.getName());
        fieldTelephone.setText(student.getTelephone());
        fieldBirthDate.setText(student.getBirthDate() != null ? student.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "");

        if (address != null) {
            City city = address.getCity();
            State state = address.getState();

            if (state != null) {
                comboState.setSelectedItem(state.getName());
                loadCities(state);
            }

            if (city != null) {
                comboCity.setSelectedItem(city.getName());
            }

            fieldStreet.setText(address.getStreet());
            fieldNeighorhood.setText(address.getNeighborhood());
        }
    }

    public void addModalitiesInTable(List<Modality> modalities) {
        for (Modality modality : modalities) {
            tblModel.addRow(new String[]{modality.getDescription(), modality.getValue().toString()});
        }
    }

    public void clear() {
        fieldName.setText(null);
        fieldTelephone.setText(null);
        fieldBirthDate.setText(null);
        fieldStreet.setText(null);
        fieldNeighorhood.setText(null);
        comboState.setSelectedIndex(0);
        comboCity.setSelectedIndex(0);
        comboCity.addItem("Selecione uma cidade.");
        comboState.addItem("Selecione um estado");
        deleteRowsTableModalities();
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
