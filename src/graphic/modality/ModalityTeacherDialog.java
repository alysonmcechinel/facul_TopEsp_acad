package graphic.modality;

import database.models.user.User;
import database.models.user.UserTypeEnum;
import database.service.Service;

import javax.lang.model.type.NullType;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ModalityTeacherDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable tblTeacher;
    private DefaultTableModel tableModel;

    private Service<User> service = new Service(User.class);

    private List<User> teachers = new ArrayList<>();
    private Function<List<User>, NullType> functionOnTeacherChanged;

    public ModalityTeacherDialog() {
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
        tblTeacher.setModel(tableModel);
        setContent();
    }

    static ModalityTeacherDialog open(Function<List<User>, NullType> functionOnTeacherChanged){
        ModalityTeacherDialog modalityTeacherDialog = new ModalityTeacherDialog();
        modalityTeacherDialog.setFunctionOnTeacherChanged(functionOnTeacherChanged);
        modalityTeacherDialog.setVisible(true);

        return modalityTeacherDialog;
    }

    public void setFunctionOnTeacherChanged(Function<List<User>, NullType> functionOnTeacherChanged){
        this.functionOnTeacherChanged = functionOnTeacherChanged;
    }

    private void setContent(){
        HashMap<String, Object> params = new HashMap<>();
        params.put("type", UserTypeEnum.PROFESSOR);

        teachers = service.findAllAndFilter(params);

        for(User teacher : teachers){
            tableModel.addRow(new String[]{teacher.getName()});
        }
    }

    private void onOK() {
        List<User> teachersChanged = new ArrayList<>();
        int [] selectedRows = tblTeacher.getSelectedRows();

        for(int selectedRow : selectedRows){
            teachersChanged.add(teachers.get(selectedRow));
        }

        if(functionOnTeacherChanged != null){
            functionOnTeacherChanged.apply(teachersChanged);
        }

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        ModalityTeacherDialog dialog = new ModalityTeacherDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
