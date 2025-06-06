package view.dialogs;

import model.entities.Tarefa;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Properties;

public class DialogCriarTarefa extends JDialog {

    private JTextField txtTitulo;
    private JTextArea txtDescricao;
    private JDatePickerImpl datePicker;
    private JButton btnCriar;
    private JButton btnCancelar;

    private int idEquipe;  // para associar a tarefa à equipe

    private OnTarefaCriadaListener listener;

    // Interface para callback
    public interface OnTarefaCriadaListener {

        void onTarefaCriada(Tarefa novaTarefa);
    }

    public DialogCriarTarefa(JFrame parent, int idEquipe, OnTarefaCriadaListener listener) {
        super(parent, "Criar Tarefa", true);
        this.idEquipe = idEquipe;
        this.listener = listener;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        txtTitulo.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtTitulo.getPreferredSize().height));
        panel.add(txtTitulo);

        panel.add(new JLabel("Descrição:"));
        txtDescricao = new JTextArea(5, 20);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        JScrollPane scrollDescricao = new JScrollPane(txtDescricao);
        panel.add(scrollDescricao);

        panel.add(new JLabel("Data Limite:"));
        datePicker = criarDatePicker();
        panel.add(datePicker);

        btnCriar = new JButton("Criar");
        btnCancelar = new JButton("Cancelar");

        JPanel botoes = new JPanel();
        botoes.add(btnCriar);
        botoes.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        btnCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarTarefa();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void criarTarefa() {
        String titulo = txtTitulo.getText().trim();
        String descricao = txtDescricao.getText().trim();
        Date dataSelecionada = (Date) datePicker.getModel().getValue();

        if (titulo.isEmpty() || descricao.isEmpty() || dataSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(titulo);
        tarefa.setDescricao(descricao);
        tarefa.setDataLimite(dataSelecionada.toLocalDate());
        tarefa.setConcluida(false);
        tarefa.setIdEquipe(idEquipe);

        if (listener != null) {
            listener.onTarefaCriada(tarefa);
        }

        dispose();
    }

    private JDatePickerImpl criarDatePicker() {
        SqlDateModel model = new SqlDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        return new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());
    }
}
