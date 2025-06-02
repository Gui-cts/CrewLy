package view;

import model.Usuario;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DialogAdicionarMembro extends JDialog {

    private JTextField txtBuscar;
    private JList<String> listResultados;
    private DefaultListModel<String> listModel;
    private JButton btnAdicionar, btnCancelar;

    private int idEquipe;
    private List<Usuario> usuariosFiltrados;

    public DialogAdicionarMembro(JFrame parent, int idEquipe) {
        super(parent, "Adicionar Membro", true);
        this.idEquipe = idEquipe;

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
                try {
                    atualizarResultados();
                } catch (SQLException ex) {
                    Logger.getLogger(DialogAdicionarMembro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void removeUpdate(DocumentEvent e) {
                try {
                    atualizarResultados();
                } catch (SQLException ex) {
                    Logger.getLogger(DialogAdicionarMembro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            public void changedUpdate(DocumentEvent e) {
                try {
                    atualizarResultados();
                } catch (SQLException ex) {
                    Logger.getLogger(DialogAdicionarMembro.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        btnAdicionar.addActionListener(e -> {
            try {
                adicionarMembro();
            } catch (SQLException ex) {
                Logger.getLogger(DialogAdicionarMembro.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnCancelar.addActionListener(e -> dispose());
    }

    private void atualizarResultados() throws SQLException {
        String filtro = txtBuscar.getText().trim();
        if (filtro.length() < 2) {  // Evita consultas desnecessárias
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
