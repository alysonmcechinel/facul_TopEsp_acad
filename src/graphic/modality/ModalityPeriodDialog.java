package graphic.modality;

import database.models.modality.Modality;
import database.models.period.Period;
import database.service.Service;

import javax.lang.model.type.NullType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModalityPeriodDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblPeriod;
    private DefaultTableModel tableModel;

    private Service<Period> service = new Service(Period.class);

    private List<Period> periods = new ArrayList<>();
    private Function<List<Period>, NullType> functionOnPeriodChanged;

    public ModalityPeriodDialog() {
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
        tableModel.addColumn("Nome");
        tblPeriod.setModel(tableModel);
        setContent();
    }

    static ModalityPeriodDialog open(Function<List<Period>, NullType> functionOnPeriodChanged){
        ModalityPeriodDialog modalityPeriodDialog = new ModalityPeriodDialog();
        modalityPeriodDialog.setFunctionOnPeriodChanged(functionOnPeriodChanged);
        modalityPeriodDialog.setVisible(true);

        return modalityPeriodDialog;
    }

    public void setFunctionOnPeriodChanged(Function<List<Period>, NullType> functionOnPeriodChanged){
        this.functionOnPeriodChanged = functionOnPeriodChanged;
    }

    private void setContent(){
        periods = service.findAll();

        for (Period period : periods){
            tableModel.addRow(new String[]{period.getDescription()});
        }
    }

    private void onOK() {
        List<Period> periodsChanged = new ArrayList<>();
        int [] selectedRows = tblPeriod.getSelectedRows();

        for(int selectedRow : selectedRows){
            periodsChanged.add(periods.get(selectedRow));
        }

        if(functionOnPeriodChanged != null){
            functionOnPeriodChanged.apply(periodsChanged);
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ModalityPeriodDialog dialog = new ModalityPeriodDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
