package graphic.payment;

import database.models.payment.Payment;
import database.models.user.User;
import database.service.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaymentConsultForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblPayment;
    private JTextField txtStudent;
    private JComboBox cbStatus;
    private JButton btnSearch;
    private DefaultTableModel tableModel;

    private Service<Payment> service = new Service<>(Payment.class);

    private List<Payment> payments = new ArrayList<>();

    public PaymentConsultForm() {
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

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setContent();
            }
        });

        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.addColumn("Competência");
        tableModel.addColumn("Código aluno");
        tableModel.addColumn("Data pagamento");
        tableModel.addColumn("Valor total");
        tableModel.addColumn("Valor pago");
        tblPayment.setModel(tableModel);

        loadStatus();
        setContent();
    }

    private void loadStatus() {
        cbStatus.addItem("Todos");
        cbStatus.addItem("Pago");
        cbStatus.addItem("Aberto");
    }

    private void setContent() {
        HashMap<String, Object> params = new HashMap<>();
        Integer studentCod = 0;

        try {
            studentCod = Integer.parseInt(txtStudent.getText());
        } catch (NumberFormatException nfe) {

        }

        if ((String) cbStatus.getSelectedItem() == "Aberto") {
            payments = Payment.paymentStatusOpen(studentCod);
        } else if ((String) cbStatus.getSelectedItem() == "Pago") {
            payments = Payment.paymentStatusPaid(studentCod);
        } else {
            if (studentCod != 0) {
                params.put("student", studentCod);

                payments = service.findAllAndFilter(params);
            } else {
                payments = service.findAll();
            }
        }

        deleteRowsTablePayment();

        for (Payment payment : payments) {
            tableModel.addRow(
                    new String[]{
                            String.valueOf(payment.getReference_month()),
                            String.valueOf(payment.getId()),
                            String.valueOf(payment.getPayday()),
                            String.valueOf(payment.getAmount()),
                            String.valueOf(payment.getAmount_paid())
                    });
        }
    }

    private void deleteRowsTablePayment() {
        for (int i = tblPayment.getRowCount() - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        PaymentConsultForm dialog = new PaymentConsultForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
