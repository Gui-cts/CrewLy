package view.dialogs;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.List;
import model.entities.Usuario;

public class DialogTornarLider extends JDialog {

    private JTextField txtBuscar;
    private JList<String> listResultados;
    private DefaultListModel<String> listModel;
    private JButton btnTornarLider, btnCancelar;

    private List<Usuario> usuariosFiltrados;
    private Usuario usuario;

    public DialogTornarLider(JFrame parent, Usuario usuario) {
        super(parent, "Tornar Conta em Líder", true);
        this.usuario = usuario;  // ✅ Guardamos a referência

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panelBusca = new JPanel(new BorderLayout());
        panelBusca.add(new JLabel("Buscar usuário (nome ou email):"), BorderLayout.NORTH);

        txtBuscar = new JTextField();
        panelBusca.add(txtBuscar, BorderLayout.CENTER);
        add(panelBusca, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        listResultados = new JList<>(listModel);
        add(new JScrollPane(listResultados), BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        btnTornarLider = new JButton("Tornar Líder");
        btnCancelar = new JButton("Cancelar");
        botoes.add(btnTornarLider);
        botoes.add(btnCancelar);
        add(botoes, BorderLayout.SOUTH);

        // ✅ Atualizamos a lista conforme a busca
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

        // ✅ Ação dos botões
        btnTornarLider.addActionListener(e -> tornarLider());
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

    private void tornarLider() {
        int index = listResultados.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário.");
            return;
        }

        Usuario selecionado = usuariosFiltrados.get(index);

        // ✅ Mantemos a proteção: Não permitir que o usuário torne a si mesmo em líder
        if (selecionado.getIdUsuario() == usuario.getIdUsuario()) {
            JOptionPane.showMessageDialog(this, "Você já é um líder!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja tornar " + selecionado.getNome() + " um líder?",
                "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                selecionado.tornarLider(selecionado.getIdUsuario());
                JOptionPane.showMessageDialog(this, "Usuário agora é um líder!");
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao tornar usuário líder.");
            }
        }
    }
}
