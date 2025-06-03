package view;

import connection.Database;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.ArquivoUpload;
import model.Usuario;

public class DialogVerUploads extends JDialog {

    private JList<String> listaArquivos;
    private DefaultListModel<String> modelLista;
    private JButton btnDownload;

    private List<ArquivoUpload> uploads;

    public DialogVerUploads(JFrame parent, Usuario usuario) {
        super(parent, "Uploads de Arquivos", true);

        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        modelLista = new DefaultListModel<>();
        listaArquivos = new JList<>(modelLista);
        add(new JScrollPane(listaArquivos), BorderLayout.CENTER);

        btnDownload = new JButton("Baixar Arquivo");
        btnDownload.addActionListener(e -> baixarArquivo());

        add(btnDownload, BorderLayout.SOUTH);

        carregarUploads();
    }

    private void carregarUploads() {
        uploads = new ArrayList<>();

        String sql = "SELECT * FROM arquivos_upload";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ArquivoUpload arq = new ArquivoUpload();
                arq.setIdArquivo(rs.getInt("id_arquivo"));
                arq.setIdTarefa(rs.getInt("id_tarefa"));
                arq.setIdUsuario(rs.getInt("id_usuario"));
                arq.setNomeArquivo(rs.getString("nome_arquivo"));
                arq.setConteudo(rs.getBytes("conteudo"));

                uploads.add(arq);
                modelLista.addElement(arq.getIdArquivo() + " - " + arq.getNomeArquivo());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar uploads.");
        }
    }

    private void baixarArquivo() {
        int index = listaArquivos.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um arquivo.");
            return;
        }

        ArquivoUpload arq = uploads.get(index);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new java.io.File(arq.getNomeArquivo()));

        int option = fileChooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                java.nio.file.Files.write(fileChooser.getSelectedFile().toPath(), arq.getConteudo());
                JOptionPane.showMessageDialog(this, "Arquivo salvo com sucesso!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo: " + ex.getMessage());
            }
        }
    }
}
