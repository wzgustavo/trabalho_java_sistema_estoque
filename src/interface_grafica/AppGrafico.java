// =============================================================================
// Classe de entrada da interface grafica (metodo main separado da versao console)
// Inicia o sistema com a janela Swing em vez do terminal.
// =============================================================================
package interface_grafica;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AppGrafico {

    public static void main(String[] args) {
        // Usar o look-and-feel padrao do Java (Metal/Cross-Platform)
        // que respeita as cores customizadas definidas nos componentes.
        // O L&F do sistema (Windows) sobreescreve cores de JTextField/JComboBox.
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // Se falhar, usa o padrao do Java sem problemas
        }

        // Iniciar a interface grafica na thread de eventos do Swing (boa pratica)
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JanelaPrincipal janela = new JanelaPrincipal();
                janela.setVisible(true);
            }
        });
    }
}
