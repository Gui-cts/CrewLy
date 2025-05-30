package utils;

import javax.swing.*;
import java.awt.*;

public class PainelComImagem extends JPanel {
    private Image imagem;

    public PainelComImagem(String caminhoImagem) {
        this.imagem = new ImageIcon(getClass().getResource(caminhoImagem)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Desenha a imagem redimensionada para o tamanho do painel
        g.drawImage(imagem, 0, 0, getWidth(), getHeight(), this);
    }
}
