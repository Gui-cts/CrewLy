package view.componentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import model.entities.RelatorioTarefa;

public class PainelRelatorioTarefas extends JFrame {

    private JPanel panelRelatorios;
    private CardLayout cardLayout;

    private JComboBox<String> comboStatus;
    private JComboBox<String> comboEquipe;
    private JButton btnFiltrar, btnAnterior, btnProximo;
    private JLabel lblResultados;

    private List<RelatorioTarefa> listaRelatorios;
    private List<String> listaNomesEquipes;

    public PainelRelatorioTarefas(List<RelatorioTarefa> listaRelatorios, List<String> listaNomesEquipes) {
        this.listaRelatorios = listaRelatorios;
        this.listaNomesEquipes = listaNomesEquipes;

        setTitle("Relatório de Tarefas");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponentes();
    }

    private void initComponentes() {
        Font fonte = new Font("SansSerif", Font.PLAIN, 14);

        comboStatus = new JComboBox<>(new String[]{"Todos", "No Prazo", "Atrasada", "Concluída"});
        comboStatus.setFont(fonte);

        comboEquipe = new JComboBox<>();
        comboEquipe.addItem("Todas");
        for (String equipe : listaNomesEquipes) {
            comboEquipe.addItem(equipe);
        }
        comboEquipe.setFont(fonte);

        btnFiltrar = new JButton("Filtrar");
        btnFiltrar.setFont(fonte);
        btnFiltrar.addActionListener(e -> aplicarFiltro());

        lblResultados = new JLabel("Resultados encontrados: ");
        lblResultados.setFont(fonte);

        JPanel painelFiltros = new JPanel(new GridLayout(2, 3, 10, 5));
        painelFiltros.add(new JLabel("Status:"));
        painelFiltros.add(comboStatus);
        painelFiltros.add(btnFiltrar);
        painelFiltros.add(new JLabel("Equipe:"));
        painelFiltros.add(comboEquipe);
        painelFiltros.add(lblResultados);

        btnAnterior = new JButton("⬅ Anterior");
        btnProximo = new JButton("Próximo ➡");

        btnAnterior.setFont(fonte);
        btnProximo.setFont(fonte);

        btnAnterior.addActionListener(e -> cardLayout.previous(panelRelatorios));
        btnProximo.addActionListener(e -> cardLayout.next(panelRelatorios));

        JPanel painelNavegacao = new JPanel();
        painelNavegacao.add(btnAnterior);
        painelNavegacao.add(btnProximo);

        cardLayout = new CardLayout();
        panelRelatorios = new JPanel(cardLayout);

        JScrollPane scroll = new JScrollPane(panelRelatorios);
        scroll.getViewport().setBackground(Color.WHITE);

        setLayout(new BorderLayout(10, 10));
        add(painelFiltros, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelNavegacao, BorderLayout.SOUTH);

        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String statusSelecionado = (String) comboStatus.getSelectedItem();
        String equipeSelecionada = (String) comboEquipe.getSelectedItem();

        panelRelatorios.removeAll();
        int totalFiltrados = 0;

        for (RelatorioTarefa item : listaRelatorios) {
            boolean exibe = true;

            if (!"Todos".equals(statusSelecionado)
                    && !item.getStatusPrazo().equalsIgnoreCase(statusSelecionado)) {
                exibe = false;
            }

            if (!"Todas".equals(equipeSelecionada)
                    && !item.getNomeEquipe().equalsIgnoreCase(equipeSelecionada)) {
                exibe = false;
            }

            if (exibe) {
                JPanel card = criarCardTarefa(item);
                panelRelatorios.add(card);
                totalFiltrados++;
            }
        }

        if (totalFiltrados == 0) {
            JOptionPane.showMessageDialog(this, "Nenhum relatório encontrado com os filtros selecionados.", "Sem resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        panelRelatorios.revalidate();
        panelRelatorios.repaint();

        btnAnterior.setEnabled(panelRelatorios.getComponentCount() > 1);
        btnProximo.setEnabled(panelRelatorios.getComponentCount() > 1);
        lblResultados.setText("Resultados encontrados: " + totalFiltrados);
    }

    private JPanel criarCardTarefa(RelatorioTarefa item) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Cor de fundo condicional
        Color corFundo;
        switch (item.getStatusPrazo().toLowerCase()) {
            case "no prazo":
                corFundo = new Color(200, 255, 200); // verde claro
                break;
            case "atrasada":
                corFundo = new Color(255, 200, 200); // vermelho claro
                break;
            case "concluída":
                corFundo = new Color(200, 225, 255); // azul claro
                break;
            default:
                corFundo = Color.WHITE;
        }

        card.setBackground(corFundo);

        Font fonte = new Font("SansSerif", Font.PLAIN, 13);

        JLabel lblEquipe = new JLabel("Equipe: " + item.getNomeEquipe());
        JLabel lblTitulo = new JLabel("Título: " + item.getTituloTarefa());
        JLabel lblConcluida = new JLabel("Concluída: " + (item.isConcluida() ? "Sim" : "Não"));
        JLabel lblLimite = new JLabel("Data Limite: " + item.getDataLimite());
        JLabel lblConclusao = new JLabel("Data Conclusão: " + item.getDataConclusao());
        JLabel lblStatus = new JLabel("Status: " + item.getStatusPrazo());

        JLabel[] labels = {lblEquipe, lblTitulo, lblConcluida, lblLimite, lblConclusao, lblStatus};
        for (JLabel lbl : labels) {
            lbl.setFont(fonte);
            lbl.setForeground(Color.DARK_GRAY);
            card.add(lbl);
        }

        return card;
    }
}
