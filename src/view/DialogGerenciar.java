package view;

import javax.swing.*;
import java.awt.*;
import model.Usuario;

public class DialogGerenciar extends JDialog {

    private JButton btnTornarLider;
    private JButton btnAcessarUploads;
    private Usuario usuario;

    public DialogGerenciar(JFrame parent, Usuario usuario) {
        super(parent, "Gerenciar Usuários e Uploads", true);
        this.usuario = usuario;

        setLayout(new BorderLayout(15, 15));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel lblTitulo = new JLabel("Selecione uma ação", JLabel.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(220, 220, 220));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(lblTitulo, BorderLayout.NORTH);

        btnTornarLider = new JButton("Tornar Conta em Líder");
        btnAcessarUploads = new JButton("Acessar Uploads");

        Font fonteBotao = new Font("SansSerif", Font.PLAIN, 16);
        btnTornarLider.setFont(fonteBotao);
        btnAcessarUploads.setFont(fonteBotao);

        Dimension tamanhoBotao = new Dimension(250, 50);
        btnTornarLider.setPreferredSize(tamanhoBotao);
        btnAcessarUploads.setPreferredSize(tamanhoBotao);

        // Centraliza horizontalmente
        btnTornarLider.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAcessarUploads.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));
        painelBotoes.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        painelBotoes.setBackground(new Color(245, 245, 245));

        painelBotoes.add(Box.createVerticalGlue());
        painelBotoes.add(btnTornarLider);
        painelBotoes.add(Box.createRigidArea(new Dimension(0, 20)));
        painelBotoes.add(btnAcessarUploads);
        painelBotoes.add(Box.createVerticalGlue());

        add(painelBotoes, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnFechar.addActionListener(e -> dispose());

        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.add(btnFechar);

        add(painelRodape, BorderLayout.SOUTH);

        pack();
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(parent);

        // ➡️ Ações dos botões
        btnTornarLider.addActionListener(e -> {
            DialogTornarLider dialog = new DialogTornarLider(parent, usuario);
            dialog.setVisible(true);
        });

        btnAcessarUploads.addActionListener(e -> {
            DialogVerUploads dialog = new DialogVerUploads(parent, usuario);
            dialog.setVisible(true);
        });
    }
}
