package graphic.period;

import database.models.period.Period;
import graphic.commons.ConsultaGenericaWindow;
import lib.Observer;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PeriodForm extends JDialog implements Observer<Period> {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField fieldInitTime;
    private JTextField fieldIEndTime;
    private JTextField fieldDescription;
    private JButton consultarPeriodosButton;
    private JButton deletarPeriodoButton;

    private Period period = new Period();

    public PeriodForm() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(400, 350);
        setLocationRelativeTo(null);
        PeriodForm instance = this;

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

        consultarPeriodosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ConsultaGenericaWindow<>(Period.class, instance, new String[]{"Descrição", "Hora inicial", "Hora final"}).open();
            }
        });

        deletarPeriodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (period != null && period.getId() != null) {
                    period.delete();
                }
            }
        });
        deletarPeriodoButton.setEnabled(false);
    }

    private void onOK() {
        String mensagem = period.getId() != null ? "Atualizado com sucesso!" : "Salvo com sucesso!";

        String description = fieldDescription.getText();
        String endTime = fieldIEndTime.getText();
        String initTime = fieldInitTime.getText();

        period.setEndTime(LocalTime.parse(endTime));
        period.setInitTime(LocalTime.parse(initTime));
        period.setDescription(description);
        period.save();

        JOptionPane.showMessageDialog(null, mensagem);

        clear();

        // dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        PeriodForm dialog = new PeriodForm();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void clear() {
        fieldDescription.setText("");
        fieldInitTime.setText("");
        fieldIEndTime.setText("");
    }

    @Override
    public void update(Period o) {
        period = o;
        fieldDescription.setText(o.getDescription());
        fieldInitTime.setText(o.getInitTime().format(DateTimeFormatter.ISO_TIME));
        fieldIEndTime.setText(o.getEndTime().format(DateTimeFormatter.ISO_TIME));
        deletarPeriodoButton.setEnabled(true);
    }
}
