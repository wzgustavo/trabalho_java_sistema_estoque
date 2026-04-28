// =============================================================================
// Janela de Movimentacao de Estoque (JDialog modal)
// Permite dar entrada ou saida de estoque de um produto selecionado.
// IMPORTA classes de modelo/ — modelo/ NAO importa nada daqui.
// =============================================================================
package interface_grafica;

import modelo.Produto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JanelaMovimentacaoEstoque extends JDialog {

    // Referencia ao array de produtos
    private Produto[] produtos;
    private int totalProdutos;
    private boolean operacaoRealizada;

    // Componentes do formulario
    private JComboBox<String> comboProduto;
    private JRadioButton radioEntrada;
    private JRadioButton radioSaida;
    private JTextField campoQuantidade;
    private JLabel lblInfoProduto;
    private JLabel lblEstoqueAtual;
    private JLabel erroQuantidade;

    // Cores (mesma paleta)
    private static final Color COR_FUNDO = new Color(0x1A, 0x1A, 0x1A);
    private static final Color COR_PAINEL = new Color(0x25, 0x25, 0x25);
    private static final Color COR_BORDA = new Color(0x3A, 0x3A, 0x3A);
    private static final Color COR_TEXTO = new Color(0xF0, 0xF0, 0xF0);
    private static final Color COR_TEXTO_SEC = new Color(0xA0, 0xA0, 0xA0);
    private static final Color COR_ACENTO = new Color(0xC6, 0xFF, 0x00);
    private static final Color COR_ACENTO_HOVER = new Color(0xD4, 0xFF, 0x4D);
    private static final Color COR_VERMELHO = new Color(0xFF, 0x45, 0x45);
    private static final Color COR_VERDE = new Color(0x4C, 0xAF, 0x50);
    private static final Color COR_AMARELO = new Color(0xFF, 0xD5, 0x4F);

    // Cores para campos de entrada — fundo claro com texto escuro para legibilidade
    private static final Color COR_CAMPO_FUNDO = Color.WHITE;
    private static final Color COR_CAMPO_TEXTO = Color.BLACK;
    private static final Color COR_CAMPO_CARET = Color.BLACK;

    private static final Font FONTE_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONTE_ERRO = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONTE_INFO = new Font("Segoe UI", Font.PLAIN, 12);

    // =========================================================================
    // Construtor
    // =========================================================================
    public JanelaMovimentacaoEstoque(JFrame pai, Produto[] produtos, int totalProdutos) {
        super(pai, "Movimentacao de Estoque", true);
        this.produtos = produtos;
        this.totalProdutos = totalProdutos;
        this.operacaoRealizada = false;

        setSize(460, 460);
        setLocationRelativeTo(pai);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    // =========================================================================
    // Header
    // =========================================================================
    private JPanel criarHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        header.setBackground(COR_PAINEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel icone = new JLabel("\u21C5");
        icone.setFont(new Font("Segoe UI", Font.BOLD, 20));
        icone.setForeground(COR_ACENTO);
        header.add(icone);

        JLabel titulo = new JLabel("Movimentar Estoque");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TEXTO);
        header.add(titulo);

        return header;
    }

    // =========================================================================
    // Formulario
    // =========================================================================
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(COR_FUNDO);
        form.setBorder(new EmptyBorder(16, 24, 8, 24));

        // ComboBox de produtos
        JLabel lblProduto = new JLabel("Selecione o Produto");
        lblProduto.setFont(FONTE_LABEL);
        lblProduto.setForeground(COR_TEXTO_SEC);
        lblProduto.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblProduto);
        form.add(Box.createVerticalStrut(4));

        comboProduto = new JComboBox<String>();
        comboProduto.setFont(FONTE_CAMPO);
        comboProduto.setBackground(COR_CAMPO_FUNDO);
        comboProduto.setForeground(COR_CAMPO_TEXTO);
        comboProduto.setOpaque(true);
        comboProduto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        comboProduto.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Renderer customizado para hover verde-lima
        comboProduto.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                Component comp = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                if (isSelected) {
                    comp.setBackground(COR_ACENTO);
                    comp.setForeground(Color.BLACK);
                } else {
                    comp.setBackground(COR_CAMPO_FUNDO);
                    comp.setForeground(COR_CAMPO_TEXTO);
                }
                setBorder(new EmptyBorder(4, 8, 4, 8));
                return comp;
            }
        });

        for (int i = 0; i < totalProdutos; i++) {
            comboProduto.addItem("[" + produtos[i].getSku() + "] " + produtos[i].getNome());
        }

        comboProduto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarInfoProduto();
            }
        });

        form.add(comboProduto);
        form.add(Box.createVerticalStrut(8));

        // Info do produto selecionado
        lblInfoProduto = new JLabel(" ");
        lblInfoProduto.setFont(FONTE_INFO);
        lblInfoProduto.setForeground(COR_TEXTO_SEC);
        lblInfoProduto.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblInfoProduto);

        lblEstoqueAtual = new JLabel(" ");
        lblEstoqueAtual.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblEstoqueAtual.setForeground(COR_ACENTO);
        lblEstoqueAtual.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblEstoqueAtual);
        form.add(Box.createVerticalStrut(16));

        // Tipo de movimentacao (radio buttons)
        JLabel lblTipo = new JLabel("Tipo de Movimentacao");
        lblTipo.setFont(FONTE_LABEL);
        lblTipo.setForeground(COR_TEXTO_SEC);
        lblTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblTipo);
        form.add(Box.createVerticalStrut(6));

        JPanel painelRadios = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        painelRadios.setBackground(COR_FUNDO);
        painelRadios.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelRadios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        radioEntrada = new JRadioButton("Entrada (recebimento)");
        radioEntrada.setFont(FONTE_LABEL);
        radioEntrada.setForeground(COR_VERDE);
        radioEntrada.setBackground(COR_FUNDO);
        radioEntrada.setFocusPainted(false);
        radioEntrada.setSelected(true);

        radioSaida = new JRadioButton("Saida (venda)");
        radioSaida.setFont(FONTE_LABEL);
        radioSaida.setForeground(COR_AMARELO);
        radioSaida.setBackground(COR_FUNDO);
        radioSaida.setFocusPainted(false);

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(radioEntrada);
        grupo.add(radioSaida);

        painelRadios.add(radioEntrada);
        painelRadios.add(radioSaida);
        form.add(painelRadios);
        form.add(Box.createVerticalStrut(16));

        // Campo de quantidade
        JLabel lblQtd = new JLabel("Quantidade *");
        lblQtd.setFont(FONTE_LABEL);
        lblQtd.setForeground(COR_TEXTO_SEC);
        lblQtd.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(lblQtd);
        form.add(Box.createVerticalStrut(4));

        campoQuantidade = new JTextField();
        campoQuantidade.setFont(FONTE_CAMPO);
        campoQuantidade.setForeground(COR_CAMPO_TEXTO);
        campoQuantidade.setBackground(COR_CAMPO_FUNDO);
        campoQuantidade.setCaretColor(COR_CAMPO_CARET);
        campoQuantidade.setOpaque(true);
        campoQuantidade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        campoQuantidade.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        campoQuantidade.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(campoQuantidade);

        erroQuantidade = new JLabel(" ");
        erroQuantidade.setFont(FONTE_ERRO);
        erroQuantidade.setForeground(COR_VERMELHO);
        erroQuantidade.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(erroQuantidade);

        // Inicializar info do primeiro produto
        atualizarInfoProduto();

        return form;
    }

    // =========================================================================
    // Rodape com botoes
    // =========================================================================
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rodape.setBackground(COR_PAINEL);
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA),
                new EmptyBorder(12, 16, 12, 16)
        ));

        // Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(FONTE_BOTAO);
        btnCancelar.setForeground(COR_TEXTO);
        btnCancelar.setBackground(COR_PAINEL);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(10, 24, 10, 24)
        ));
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setOpaque(true);
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnCancelar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnCancelar.setBackground(COR_BORDA);
            }
            public void mouseExited(MouseEvent e) {
                btnCancelar.setBackground(COR_PAINEL);
            }
        });

        // Confirmar
        JButton btnConfirmar = new JButton("Confirmar");
        btnConfirmar.setFont(FONTE_BOTAO);
        btnConfirmar.setForeground(Color.BLACK);
        btnConfirmar.setBackground(COR_ACENTO);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setBorder(new EmptyBorder(10, 28, 10, 28));
        btnConfirmar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConfirmar.setOpaque(true);
        btnConfirmar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmarMovimentacao();
            }
        });
        btnConfirmar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnConfirmar.setBackground(COR_ACENTO_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btnConfirmar.setBackground(COR_ACENTO);
            }
        });

        rodape.add(btnCancelar);
        rodape.add(btnConfirmar);

        return rodape;
    }

    // =========================================================================
    // Atualiza as informacoes do produto selecionado
    // =========================================================================
    private void atualizarInfoProduto() {
        int indice = comboProduto.getSelectedIndex();
        if (indice >= 0 && indice < totalProdutos) {
            Produto p = produtos[indice];
            lblInfoProduto.setText("Categoria: " + p.getCategoria()
                    + "  |  Preco Venda: R$ " + String.format("%.2f", p.getPrecoVenda()));

            lblEstoqueAtual.setText("Estoque atual: " + p.getQuantidadeEstoque() + " unidades");

            // Cor baseada no estoque
            if (p.getQuantidadeEstoque() < 5) {
                lblEstoqueAtual.setForeground(COR_AMARELO);
            } else {
                lblEstoqueAtual.setForeground(COR_ACENTO);
            }
        }
    }

    // =========================================================================
    // Logica de confirmacao — usa metodos da classe Produto
    // =========================================================================
    private void confirmarMovimentacao() {
        erroQuantidade.setText(" ");

        int indice = comboProduto.getSelectedIndex();
        if (indice < 0 || indice >= totalProdutos) {
            return;
        }

        Produto produto = produtos[indice];

        // Validar quantidade
        int quantidade;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
        } catch (NumberFormatException ex) {
            erroQuantidade.setText("Informe um numero inteiro valido");
            return;
        }

        if (quantidade <= 0) {
            erroQuantidade.setText("Quantidade deve ser maior que zero");
            return;
        }

        if (radioEntrada.isSelected()) {
            // ENTRADA de estoque — usa metodo darEntradaEstoque da classe modelo
            produto.darEntradaEstoque(quantidade);
            operacaoRealizada = true;

            JOptionPane.showMessageDialog(this,
                    "Entrada de " + quantidade + " unidade(s) realizada!\n"
                    + "Novo estoque: " + produto.getQuantidadeEstoque(),
                    "Entrada de Estoque",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } else {
            // SAIDA de estoque — avisar se anuncio pausado
            if (!produto.isAnuncioAtivo()) {
                int resp = JOptionPane.showConfirmDialog(this,
                        "O anuncio deste produto esta PAUSADO.\n"
                        + "Em um marketplace real, nao estaria visivel.\n"
                        + "Deseja continuar com a saida?",
                        "Anuncio Pausado",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (resp != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            // Tenta dar saida usando metodo darSaidaEstoque da classe modelo
            boolean sucesso = produto.darSaidaEstoque(quantidade);
            if (sucesso) {
                operacaoRealizada = true;
                double valorVenda = produto.getPrecoVenda() * quantidade;
                JOptionPane.showMessageDialog(this,
                        "Saida de " + quantidade + " unidade(s) realizada!\n"
                        + "Novo estoque: " + produto.getQuantidadeEstoque() + "\n"
                        + "Valor da venda: R$ " + String.format("%.2f", valorVenda),
                        "Saida de Estoque",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                erroQuantidade.setText("Estoque insuficiente! Disponivel: "
                        + produto.getQuantidadeEstoque());
            }
        }
    }

    // Getter para verificar se operacao foi realizada
    public boolean isOperacaoRealizada() {
        return operacaoRealizada;
    }
}
