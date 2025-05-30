package utils;

import javax.swing.*;
import java.awt.*;

public class ImagemUtil {

    public static void setImagemDeFundo(JPanel painel, String caminhoImagem) {
        painel.setLayout(null);  // Permite posicionar a imagem livremente

        ImageIcon imagem = new ImageIcon(ImagemUtil.class.getResource(caminhoImagem));
        JLabel labelImagem = new JLabel(imagem);
        labelImagem.setBounds(0, 0, painel.getWidth(), painel.getHeight());

        painel.add(labelImagem);
        painel.setComponentZOrder(labelImagem, painel.getComponentCount() - 1);
    }
}
