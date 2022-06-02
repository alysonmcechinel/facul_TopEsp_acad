package graphic.payment;

import database.models.modality.Student;
import database.models.payment.Payment;
import database.models.user.User;

import javax.swing.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class PaymentForm extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField txtStudent;
    private JTextField txtPayday;
    private JButton btnSearch;
    private JTextField txtAmountPaid;
    private JTextField txtAmount;
    private JTextField txtReferenceMonth;

    private Payment payment = new Payment();
    private Student selectedStudent;
    private BigDecimal amount;

    public PaymentForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(800, 150);
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
                PaymentUserDialog
                        .open(student -> {
                            selectedStudent = student;
                            addStudent(selectedStudent);
                            setAmount();
                            return null;
                        });
            }
        });
    }

    private void addStudent(Student student){
        selectedStudent = student;

        txtStudent.setText(student.getName());
    }

    private void setAmount(){
        this.amount = Payment.totalValueModalities(selectedStudent);

        txtAmount.setText(String.valueOf(amount));
    }

    private void onOK() {
        Boolean gravar = true;
        String payDay = txtPayday.getText();
        String referecedMounth = txtReferenceMonth.getText();

        BigDecimal amountPaid = BigDecimal.ZERO;

        try{
            amountPaid = BigDecimal.valueOf(Double.parseDouble(txtAmountPaid.getText()));
        }catch (NumberFormatException nfe){
            JOptionPane.showMessageDialog(null, "O campo 'valor pago' deve ser um decimal.");
            gravar = false;
        }

        if(payDay.isEmpty()){
            JOptionPane.showMessageDialog(null, "Informe uma data de pagamento.");
            gravar = false;
        }else if(referecedMounth.isEmpty()){
            JOptionPane.showMessageDialog(null, "Informe uma competência.");
            gravar = false;
        }

        if(gravar){
            payment.setAmount_paid(amountPaid);
            payment.setAmount(this.amount);
            payment.setPayday(LocalDate.parse(payDay));
            payment.setReference_month(LocalDate.parse(referecedMounth));
            payment.setStudent(selectedStudent);

            payment.save();

            JOptionPane.showMessageDialog(null, "Mensalidade cadastrada com sucesso!");

            clear();
        }
    }

    private void clear(){
        txtStudent.setText(null);
        txtAmount.setText(null);
        txtAmountPaid.setText(null);
        txtPayday.setText(null);
        txtReferenceMonth.setText(null);
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        PaymentForm dialog = new PaymentForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
