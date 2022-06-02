package lib;

import javax.swing.*;
import java.io.File;

public class MasterUtil {

    public static void main(String[] args) {
        MasterUtil.backup();
    }

    public static void backup() {
        File dir = new File("./resource/backup");
        File arquivo = new File("./resource/backup/dump.sql");

        if (!dir.isDirectory()) {
            dir.mkdir();
        }

        try {
            if (arquivo.isFile()) {
                if (JOptionPane.showConfirmDialog(null, "Deseja sobrescrever o arquivo?") == JOptionPane.YES_OPTION) {
                    arquivo.delete();
                } else {
                    JOptionPane.showMessageDialog(null, "Backup cancelado pelo usuaio.");
                    return;
                }
            }

            if (new File("./backupRunner.bat").isFile()) {
                Process process = Runtime.getRuntime().exec("./backupRunner.bat");
                process.waitFor();

                if (process.exitValue() == 0) {
                    JOptionPane.showMessageDialog(null, "Backup concluido com sucesso");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao fazer backup");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Exceção no backup: " + e.getMessage());
        }

    }

}
