package view;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.sql.SQLException;

import dao.EquipeDAO;


public class DialogCriarEquipe extends JDialog {

    private int idLider;
    private TelaEquipeLider parent;

    private JTextField txtNomeEquipe;
    private JTextArea txtDescricao;

    public DialogCriarEquipe(TelaEquipeLider parent, int idLider) {
        super(parent, "Criar Nova Equipe", true);
        this.parent = parent;
        this.idLider = idLider;

        initComponents();
        setLocationRelativeTo(parent);
        pack();
    }

    private void initComponents() {
        JLabel lblNome = new JLabel("Nome da Equipe:");
        txtNomeEquipe = new JTextField(20);

        JLabel lblDescricao = new JLabel("Descrição:");
        txtDescricao = new JTextArea(4, 20);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);

        JButton btnCriar = new JButton("Criar");
        JButton btnCancelar = new JButton("Cancelar");

        btnCriar.addActionListener(e -> criarEquipe());
        btnCancelar.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(lblNome, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtNomeEquipe, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(lblDescricao, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollDescricao, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnCriar);
        btnPanel.add(btnCancelar);

        panel.add(btnPanel, gbc);

        getContentPane().add(panel);
    }

    private void criarEquipe() {
        String nome = txtNomeEquipe.getText().trim();
        String descricao = txtDescricao.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da equipe é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            EquipeDAO dao = new EquipeDAO();
            dao.criarEquipe(nome, idLider, descricao);
            JOptionPane.showMessageDialog(this, "Equipe criada com sucesso!");
            parent.carregarEquipesUsuario();  // atualiza automaticamente
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao criar equipe: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
