package view.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JOptionPane;
import model.entities.Tarefa;
import model.entities.Usuario;
import model.entities.Competencia;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class DialogAdicionarResponsavel extends JDialog {

    private JTextField txtBuscar;
    private JComboBox<Competencia> comboCompetencias;
    private JList<String> listResultados;
    private DefaultListModel<String> listModel;
    private JButton btnAdicionar, btnCancelar;

    private Tarefa tarefa;
    private List<Usuario> usuariosFiltrados;

    public DialogAdicionarResponsavel(JFrame parent, Tarefa tarefa) {
        super(parent, "Adicionar Responsável", true);
        this.tarefa = tarefa;

        setSize(500, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Painel de busca e filtro
        JPanel panelBusca = new JPanel();
        panelBusca.setLayout(new BoxLayout(panelBusca, BoxLayout.Y_AXIS));

        // Linha 1: Buscar
        JPanel linha1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha1.add(new JLabel("Buscar (nome/email):"));
        txtBuscar = new JTextField(15);
        linha1.add(txtBuscar);

        // Linha 2: Filtro por competência
        JPanel linha2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linha2.add(new JLabel("Filtrar por competência:"));
        comboCompetencias = new JComboBox<>();
        comboCompetencias.addItem(new Competencia(0, "Todas as competências"));

        List<Competencia> todas = Competencia.buscarTodas();
        System.out.println("Quantidade de competências carregadas: " + todas.size());
        for (Competencia c : todas) {
            System.out.println("Adicionando competência: " + c.getNome());
            comboCompetencias.addItem(c);
        }

        linha2.add(comboCompetencias);

        // Adiciona linhas ao painel principal de busca
        panelBusca.add(linha1);
        panelBusca.add(linha2);

        add(panelBusca, BorderLayout.NORTH);

        // Lista de resultados
        listModel = new DefaultListModel<>();
        listResultados = new JList<>(listModel);
        add(new JScrollPane(listResultados), BorderLayout.CENTER);

        // Botões
        JPanel botoes = new JPanel();
        btnAdicionar = new JButton("Adicionar");
        btnCancelar = new JButton("Cancelar");
        botoes.add(btnAdicionar);
        botoes.add(btnCancelar);
        add(botoes, BorderLayout.SOUTH);

        // Listeners
        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                atualizarResultadosComTratamento();
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                atualizarResultadosComTratamento();
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                atualizarResultadosComTratamento();
            }
        });

        comboCompetencias.addActionListener(e -> atualizarResultadosComTratamento());

        btnAdicionar.addActionListener(e -> adicionarResponsavel());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void atualizarResultadosComTratamento() {
        try {
            atualizarResultados();
        } catch (SQLException ex) {
            Logger.getLogger(DialogAdicionarResponsavel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizarResultados() throws SQLException {
        String filtro = txtBuscar.getText().trim();
        Competencia compSelecionada = (Competencia) comboCompetencias.getSelectedItem();

        if (filtro.length() < 2 && (compSelecionada == null || compSelecionada.getId() == 0)) {
            listModel.clear();
            return;
        }

        Usuario usuarioDAO = new Usuario();
        usuariosFiltrados = usuarioDAO.buscarUsuariosPorNomeEmailECompetencia(
                filtro,
                (compSelecionada != null && compSelecionada.getId() != 0) ? compSelecionada.getId() : null
        );

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
            if (tarefa.verificarUsuarioResponsavel(selecionado.getIdUsuario(), tarefa.getIdTarefa())) {
                JOptionPane.showMessageDialog(this, "Usuário já é responsável por essa tarefa.");
                return;
            }

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
