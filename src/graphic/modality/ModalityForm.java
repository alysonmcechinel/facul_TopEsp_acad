package graphic.modality;

import database.models.modality.Modality;
import database.models.period.Period;
import database.models.user.User;
import graphic.commons.ConsultaGenericaWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.List;

public class ModalityForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblTeacher;
    private JTable tblperiod;
    private JTextField txtName;
    private JTextField txtValue;
    private JButton btnDeleteTeacher;
    private JButton btnSearchTeacher;
    private JButton btnDeletePeriod;
    private JButton btnSearchPeriod;

    private Modality modality = new Modality();
    private List<User> selectedUsers;
    private List<Period> selectedPeriods;

    private DefaultTableModel tblModelTeacher;
    private DefaultTableModel tblModel;

    public ModalityForm() {
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

        tblModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblModelTeacher = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblModel.addColumn("Nome");
        tblModelTeacher.addColumn("Nome");

        tblperiod.setModel(tblModel);
        tblTeacher.setModel(tblModelTeacher);

        btnSearchPeriod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModalityPeriodDialog
                        .open(periods -> {
                            selectedPeriods = periods;
                           addPeriodsInTable(periods);
                           return null;
                        });
            }
        });

        btnSearchTeacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ModalityTeacherDialog
                        .open(teachers -> {
                            selectedUsers = teachers;
                            addTeachersInTable(teachers);
                            return null;
                        });
            }
        });

        btnDeletePeriod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = tblperiod.getSelectedRows();

                for(int selectedRow : selectedRows){
                    tblModel.removeRow(selectedRow);
                }
            }
        });

        btnDeleteTeacher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int [] selectedRows = tblTeacher.getSelectedRows();

                for(int selectedRow : selectedRows){
                    tblModelTeacher.removeRow(selectedRow);
                }
            }
        });
    }

    public void addPeriodsInTable(List<Period> periods){
        for(Period period : periods){
            tblModel.addRow(new String[]{period.getDescription()});
        }
    }

    public void addTeachersInTable(List<User> teachers){
        for(User teacher : teachers){
            tblModelTeacher.addRow(new String[]{teacher.getName()});
        }
    }

    private void onOK() {
        User user = new User();
        Period period = new Period();

        Boolean gravar = true;

        BigDecimal value = BigDecimal.ZERO;

        String name = txtName.getText();
        try {
            value = BigDecimal.valueOf(Double.parseDouble(txtValue.getText()));
        }catch (NumberFormatException nf){
            JOptionPane.showMessageDialog(null, "O campo 'valor' deve ser um numero!");
            gravar = false;
        }

        if(selectedPeriods.isEmpty()){
            JOptionPane.showMessageDialog(null, "Selecione pelo menos um período.");
            gravar = false;
        }else{
            List<Period> periods = modality.getPeriod();
            periods.addAll(selectedPeriods);
        }

        if(gravar & selectedUsers.isEmpty()){
            JOptionPane.showMessageDialog(null, "Selecione pelo menos um professor.");
            gravar = false;
        }else{
            List<User> users = modality.getTeacher();
            users.addAll(selectedUsers);
        }

        if(gravar){
            modality.setDescription(name);
            modality.setValue(value);

            modality.save();

            clear();

            JOptionPane.showMessageDialog(null, "Modalidade cadastrada com sucesso.");
        }

        //dispose();
    }

    private void clear(){
        txtName.setText(null);
        txtValue.setText(null);
        deleteRowsTablePeriods();
        deleteRowsTableTeachers();
    }

    private void deleteRowsTablePeriods() {
        for (int i = tblModel.getRowCount() - 1; i >= 0; i--) {
            tblModel.removeRow(i);
        }
    }

    private void deleteRowsTableTeachers() {
        for (int i = tblModelTeacher.getRowCount() - 1; i >= 0; i--) {
            tblModelTeacher.removeRow(i);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ModalityForm dialog = new ModalityForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
