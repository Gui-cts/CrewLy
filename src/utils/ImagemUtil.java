package utils;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImagemUtil {

    public static void ajustarImagem(JLabel label, String caminhoInterno) {
    URL urlImagem = ImagemUtil.class.getResource(caminhoInterno);
    if (urlImagem != null) {
        ImageIcon imagemOriginal = new ImageIcon(urlImagem);
        Image imagem = imagemOriginal.getImage();

        int labelLargura = label.getWidth();
        int labelAltura = label.getHeight();

        int imagemOriginalLargura = imagemOriginal.getIconWidth();
        int imagemOriginalAltura = imagemOriginal.getIconHeight();

        if (labelLargura > 0 && labelAltura > 0) {
            // Calcula a proporção
            double proporcaoLabel = (double) labelLargura / labelAltura;
            double proporcaoImagem = (double) imagemOriginalLargura / imagemOriginalAltura;

            int novaLargura, novaAltura;

            if (proporcaoImagem > proporcaoLabel) {
                // Imagem mais larga que o espaço — limita pela largura
                novaLargura = labelLargura;
                novaAltura = (int) (labelLargura / proporcaoImagem);
            } else {
                // Imagem mais alta que o espaço — limita pela altura
                novaAltura = labelAltura;
                novaLargura = (int) (labelAltura * proporcaoImagem);
            }

            Image imagemRedimensionada = imagem.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(imagemRedimensionada));
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setVerticalAlignment(JLabel.CENTER);
        }
    } else {
        System.err.println("Imagem não encontrada: " + caminhoInterno);
    }
  }
}
