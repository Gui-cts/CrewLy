package view.dialogs;

import connection.Database;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import model.entities.Tarefa;
import model.entities.Usuario;
import java.sql.*;
import model.entities.ArquivoUpload;

public class DialogDetalhesTarefa extends JDialog {

    private JLabel lblTitulo;
    private JTextArea txtDescricao;
    private JLabel lblResponsavel;
    private JLabel lblDataLimite;
    private JButton btnUpload;
    private JButton btnFechar;
    private JButton btnAdicionarResponsaveis;
    private JButton btnConcluir;

    private JFileChooser fileChooser;

    private Usuario usuario;
    private Tarefa tarefa;

    public DialogDetalhesTarefa(JFrame parent, boolean modal, Tarefa tarefa, Usuario usuario) {
        super(parent, "Detalhes da Tarefa", modal);
        this.usuario = usuario;
        this.tarefa = tarefa;

        try {
            tarefa.setResponsaveis(tarefa.buscarResponsaveis());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar responsáveis: " + e.getMessage());
        }

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

        // Mostrar responsáveis
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
        btnUpload.addActionListener(e -> realizarUpload());
        panel.add(btnUpload);

        // Se for líder, mostrar botão de adicionar responsáveis
        if (usuario.getIdTipo() == 2) {
            btnAdicionarResponsaveis = new JButton("Adicionar Responsáveis");
            btnAdicionarResponsaveis.addActionListener(e -> abrirDialogAdicionarResponsaveis());
            panel.add(btnAdicionarResponsaveis);
        }

        // Botão de fechar
        btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        panel.add(btnFechar);

        add(panel, BorderLayout.CENTER);

        btnConcluir = new JButton("Concluir Tarefa");
        btnConcluir.addActionListener(e -> confirmarConclusao());
        panel.add(btnConcluir);

    }

    private void realizarUpload() {
        fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File arquivo = fileChooser.getSelectedFile();

            try {
                // Lê conteúdo do arquivo
                byte[] conteudo = java.nio.file.Files.readAllBytes(arquivo.toPath());

                // Cria o objeto ArquivoUpload
                ArquivoUpload upload = new ArquivoUpload();
                upload.setIdTarefa(tarefa.getIdTarefa());
                upload.setIdUsuario(usuario.getIdUsuario());
                upload.setNomeArquivo(arquivo.getName());
                upload.setConteudo(conteudo);

                // Salva no banco
                salvarArquivoUpload(upload);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao ler ou enviar o arquivo: " + ex.getMessage());
            }
        }
    }

    private void abrirDialogAdicionarResponsaveis() {
        DialogAdicionarResponsavel dialog = new DialogAdicionarResponsavel((JFrame) getParent(), tarefa);
        dialog.setVisible(true);
    }

    private void confirmarConclusao() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja concluir esta tarefa?",
                "Confirmar Conclusão",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                tarefa.concluirTarefa();
                JOptionPane.showMessageDialog(this, "Tarefa concluída com sucesso!");

                lblTitulo.setText("Título: " + tarefa.getTitulo() + " (Concluída)");
                btnConcluir.setEnabled(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao concluir tarefa.");
            }
        }
    }

    private void salvarArquivoUpload(ArquivoUpload arquivoUpload) {
        String sql = "INSERT INTO arquivos_upload (id_tarefa, id_usuario, nome_arquivo, conteudo) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, arquivoUpload.getIdTarefa());
            stmt.setInt(2, arquivoUpload.getIdUsuario());
            stmt.setString(3, arquivoUpload.getNomeArquivo());
            stmt.setBytes(4, arquivoUpload.getConteudo());

            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Arquivo enviado com sucesso!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao enviar arquivo: " + e.getMessage());
        }
    }
}
