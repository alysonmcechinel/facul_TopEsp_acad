package graphic.payment;

import database.models.modality.Student;
import database.models.user.User;
import database.service.Service;

import javax.lang.model.type.NullType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PaymentUserDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblStudent;
    private DefaultTableModel tableModel;

    private Service<Student> service = new Service(Student.class);

    private List<Student> students = new ArrayList<>();
    private Student student = new Student();
    private Function<Student, NullType> functionOnStudentChanged;


    public PaymentUserDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(300, 300);
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

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn("Código");
        tableModel.addColumn("Nome");
        tblStudent.setModel(tableModel);
        setContent();
    }

    static PaymentUserDialog open(Function<Student, NullType> functionOnStudentChanged){
        PaymentUserDialog paymentUserDialog = new PaymentUserDialog();
        paymentUserDialog.setFunctionOnStudentChanged(functionOnStudentChanged);
        paymentUserDialog.setVisible(true);

        return paymentUserDialog;
    }

    public void setFunctionOnStudentChanged(Function<Student, NullType> functionOnStudentChanged){
        this.functionOnStudentChanged = functionOnStudentChanged;
    }

    private void setContent(){
        students = service.findAll();

        for(Student student : students){
            tableModel.addRow(new String[]{String.valueOf(student.getId()), student.getName()});
        }
    }

    private void onOK() {
        int [] selectedRows = tblStudent.getSelectedRows();

        if (selectedRows.length > 1){
            JOptionPane.showMessageDialog(null, "Selecione apenas um empregado.");
        } else if(functionOnStudentChanged != null){
            student = students.get(tblStudent.getSelectedRow());

            functionOnStudentChanged.apply(student);

            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        PaymentUserDialog dialog = new PaymentUserDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
