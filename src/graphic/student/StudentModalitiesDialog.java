package graphic.student;

import database.models.modality.Modality;
import database.service.Service;

import javax.lang.model.type.NullType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class StudentModalitiesDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblModalities;
    private DefaultTableModel tableModel;

    private Service<Modality> service = new Service(Modality.class);
    private List<Modality> modalities = new ArrayList<>();

    private Function<List<Modality>, NullType> functionOnModalitiesChanged;

    public StudentModalitiesDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(200, 200);
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
        tableModel.addColumn("Valor");
        tblModalities.setModel(tableModel);
        setContent();
    }

    static StudentModalitiesDialog open(Function<List<Modality>, NullType> functionOnModalitiesChanged) {
        StudentModalitiesDialog studentModalitiesDialog = new StudentModalitiesDialog();
        studentModalitiesDialog.setFunctionOnModalitiesChanged(functionOnModalitiesChanged);
        studentModalitiesDialog.setVisible(true);
        return studentModalitiesDialog;
    }

    public void setFunctionOnModalitiesChanged(Function<List<Modality>, NullType> functionOnModalitiesChanged) {
        this.functionOnModalitiesChanged = functionOnModalitiesChanged;
    }

    private void setContent() {
        modalities = service.findAll();

        for (Modality modality : modalities) {
            tableModel.addRow(new String[]{modality.getDescription(), modality.getValue().toString()});
        }
    }

    private void onOK() {
        List<Modality> modalitiesChanged = new ArrayList<>();
        int[] selectedRows = tblModalities.getSelectedRows();

        for (int selectedRow : selectedRows) {
            modalitiesChanged.add(modalities.get(selectedRow));
        }

        if (functionOnModalitiesChanged != null) {
            functionOnModalitiesChanged.apply(modalitiesChanged);
        }
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        StudentModalitiesDialog dialog = new StudentModalitiesDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
