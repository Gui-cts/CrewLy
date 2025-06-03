package view;

import java.awt.BorderLayout;
import javax.swing.JOptionPane;
import model.Tarefa;
import model.Usuario;
import java.sql.*;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DialogAdicionarResponsavel extends JDialog {

    private JTextField txtBuscar;
    private JList<String> listResultados;
    private DefaultListModel<String> listModel;
    private JButton btnAdicionar, btnCancelar;

    private Tarefa tarefa;
    private List<Usuario> usuariosFiltrados;

    public DialogAdicionarResponsavel(JFrame parent, Tarefa tarefa) {
        super(parent, "Adicionar Responsável", true);
        this.tarefa = tarefa;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelBusca = new JPanel(new BorderLayout());
        panelBusca.add(new JLabel("Buscar (nome ou email):"), BorderLayout.NORTH);

        txtBuscar = new JTextField();
        panelBusca.add(txtBuscar, BorderLayout.CENTER);
        add(panelBusca, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listResultados = new JList<>(listModel);
        add(new JScrollPane(listResultados), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        btnAdicionar = new JButton("Adicionar");
        btnCancelar = new JButton("Cancelar");
        botoes.add(btnAdicionar);
        botoes.add(btnCancelar);
        add(botoes, BorderLayout.SOUTH);

        // Listeners
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                atualizarResultados();
            }

            public void removeUpdate(DocumentEvent e) {
                atualizarResultados();
            }

            public void changedUpdate(DocumentEvent e) {
                atualizarResultados();
            }
        });

        btnAdicionar.addActionListener(e -> adicionarResponsavel());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void atualizarResultados() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.length() < 2) {
            listModel.clear();
            return;
        }

        Usuario usuarioDAO = new Usuario();
        usuariosFiltrados = usuarioDAO.buscarUsuariosPorNomeOuEmail(filtro);
        listModel.clear();
        for (Usuario u : usuariosFiltrados) {
            listModel.addElement(u.getNome() + " - " + u.getEmail());
        }
    }

    private void adicionarResponsavel() {
        int index = listResultados.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            return;
        }

        Usuario selecionado = usuariosFiltrados.get(index);
        try {
            // Verificar se o usuário já está como responsável
            if (tarefa.verificarUsuarioResponsavel(selecionado.getIdUsuario(), tarefa.getIdTarefa())) {
                JOptionPane.showMessageDialog(this, "Usuário já é responsável por essa tarefa.");
                return;
            }

            // Verificar se o usuário está na equipe
            if (!tarefa.usuarioEstaNaEquipe(selecionado.getIdUsuario())) {
                JOptionPane.showMessageDialog(this, "Usuário não faz parte da equipe desta tarefa.");
                return;
            }

            tarefa.adicionarResponsavel(selecionado.getIdUsuario(), tarefa.getIdTarefa());
            JOptionPane.showMessageDialog(this, "Responsável adicionado com sucesso!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao adicionar responsável.");
        }
    }
}
