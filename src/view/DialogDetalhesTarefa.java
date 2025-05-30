package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import model.Tarefa;
import model.Usuario;

public class DialogDetalhesTarefa extends JDialog {

    private JLabel lblTitulo;
    private JTextArea txtDescricao;
    private JLabel lblResponsavel;
    private JLabel lblDataLimite;
    private JButton btnUpload;
    private JButton btnFechar;

    private JFileChooser fileChooser;

    public DialogDetalhesTarefa(JFrame parent, boolean modal, Tarefa tarefa) {
        super(parent, "Detalhes da Tarefa", modal);
        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Painel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        lblTitulo = new JLabel("Título: " + tarefa.getTitulo());
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo);

        txtDescricao = new JTextArea(tarefa.getDescricao());
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtDescricao);
        scroll.setPreferredSize(new Dimension(350, 100));
        panel.add(new JLabel("Descrição:"));
        panel.add(scroll);

        // Mostrar todos os responsáveis
        StringBuilder responsaveisText = new StringBuilder();

        if (tarefa.getResponsaveis() != null && !tarefa.getResponsaveis().isEmpty()) {
            boolean primeiro = true;
            for (Usuario u : tarefa.getResponsaveis()) {
                String[] partesNome = u.getNome().split(" ");
                String primeiroNome = partesNome[0];
                String inicialSobrenome = "";

                if (partesNome.length > 1) {
                    inicialSobrenome = partesNome[1].substring(0, 1).toUpperCase();
                }

                if (!primeiro) {
                    responsaveisText.append(", ");
                }
                primeiro = false;

                responsaveisText.append(primeiroNome);
                if (!inicialSobrenome.isEmpty()) {
                    responsaveisText.append(" ").append(inicialSobrenome).append(".");
                }
            }
        } else {
            responsaveisText.append("Nenhum");
        }
        lblResponsavel = new JLabel("Responsável(is): " + responsaveisText.toString());
        panel.add(lblResponsavel);

        lblDataLimite = new JLabel("Data Limite: " + tarefa.getDataLimite());
        panel.add(lblDataLimite);

        // Botão de upload
        btnUpload = new JButton("Upload de Arquivo");
        btnUpload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarUpload();
            }
        });
        panel.add(btnUpload);

        // Botão de fechar
        btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        panel.add(btnFechar);

        add(panel, BorderLayout.CENTER);
    }

    private void realizarUpload() {
        fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(this, "Arquivo selecionado: " + arquivo.getName());
            // Aqui você pode chamar um método para enviar o arquivo ao banco de dados.
        }
    }
}
