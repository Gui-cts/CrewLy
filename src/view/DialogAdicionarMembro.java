package view;

import model.Competencia;
import model.Usuario;
import controller.Sistema;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogAdicionarMembro extends JDialog {

    private JTextField txtBuscar;
    private JList<String> listResultados;
    private DefaultListModel<String> listModel;
    private JButton btnAdicionar, btnCancelar;
    private JComboBox<String> comboCompetencias;

    private int idEquipe;
    private List<Usuario> usuariosFiltrados;
    private List<Competencia> listaCompetencias;

    public DialogAdicionarMembro(JFrame parent, int idEquipe) {
        super(parent, "Adicionar Membro", true);
        this.idEquipe = idEquipe;

        setSize(400, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelBusca = new JPanel(new BorderLayout(5, 5));
        panelBusca.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBusca.add(new JLabel("Buscar (nome ou email):"), BorderLayout.NORTH);

        txtBuscar = new JTextField();
        panelBusca.add(txtBuscar, BorderLayout.CENTER);

        comboCompetencias = new JComboBox<>();
        comboCompetencias.addItem("Todas as competências");
        Sistema sistema = new Sistema();
        listaCompetencias = sistema.buscarTodasCompetencias();
        for (Competencia c : listaCompetencias) {
            comboCompetencias.addItem(c.getNome());
        }
        panelBusca.add(comboCompetencias, BorderLayout.SOUTH);

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

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { atualizarComFiltro(); }
            public void removeUpdate(DocumentEvent e) { atualizarComFiltro(); }
            public void changedUpdate(DocumentEvent e) { atualizarComFiltro(); }
        });

        comboCompetencias.addActionListener(e -> atualizarComFiltro());

        btnAdicionar.addActionListener(e -> {
            try {
                adicionarMembro();
            } catch (SQLException ex) {
                Logger.getLogger(DialogAdicionarMembro.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnCancelar.addActionListener(e -> dispose());
    }

    private void atualizarComFiltro() {
        try {
            atualizarResultados();
        } catch (SQLException ex) {
            Logger.getLogger(DialogAdicionarMembro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizarResultados() throws SQLException {
        String nomeOuEmail = txtBuscar.getText().trim();
        if (nomeOuEmail.length() < 2) {
            listModel.clear();
            return;
        }

        String competenciaSelecionada = (String) comboCompetencias.getSelectedItem();
        Integer idCompetencia = null;

        if (!"Todas as competências".equals(competenciaSelecionada)) {
            for (Competencia c : listaCompetencias) {
                if (c.getNome().equals(competenciaSelecionada)) {
                    idCompetencia = c.getId();
                    break;
                }
            }
        }

        Usuario usuarioDAO = new Usuario();
        usuariosFiltrados = usuarioDAO.buscarUsuariosPorNomeEmailECompetencia(nomeOuEmail, idCompetencia);

        listModel.clear();
        for (Usuario u : usuariosFiltrados) {
            listModel.addElement(u.getNome() + " - " + u.getEmail());
        }
    }

    private void adicionarMembro() throws SQLException {
        int index = listResultados.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            return;
        }

        Usuario selecionado = usuariosFiltrados.get(index);

        Usuario usuarioDAO = new Usuario();
        boolean jaMembro = usuarioDAO.verificarUsuarioNaEquipe(selecionado.getIdUsuario(), idEquipe);
        if (jaMembro) {
            JOptionPane.showMessageDialog(this, "Usuário já é membro da equipe.");
            return;
        }
        usuarioDAO.adicionarUsuarioNaEquipe(selecionado.getIdUsuario(), idEquipe);
        JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
        dispose();
    }
}