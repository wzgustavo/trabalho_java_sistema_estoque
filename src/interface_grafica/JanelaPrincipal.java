// =============================================================================
// Janela Principal da Interface Grafica (Swing)
// Exibe a tabela de produtos, barra de acoes e barra de status.
// IMPORTA classes de modelo/ — modelo/ NAO importa nada daqui.
// =============================================================================
package interface_grafica;

import modelo.Produto;
import modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JanelaPrincipal extends JFrame {

    // -------------------------------------------------------------------------
    // Dados compartilhados (mesmo array que a versao console usaria)
    // -------------------------------------------------------------------------
    private Produto[] produtos;
    private int totalProdutos;
    private Categoria[] categorias;
    private int totalCategorias;

    // -------------------------------------------------------------------------
    // Componentes visuais
    // -------------------------------------------------------------------------
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private JLabel lblStatus;
    private JLabel lblTotalProdutos;
    private JLabel lblValorEstoque;
    private JLabel lblPausados;

    // -------------------------------------------------------------------------
    // Paleta de cores — tema escuro AnuncioLab
    // -------------------------------------------------------------------------
    private static final Color COR_FUNDO = new Color(0x1A, 0x1A, 0x1A);
    private static final Color COR_PAINEL = new Color(0x25, 0x25, 0x25);
    private static final Color COR_BORDA = new Color(0x3A, 0x3A, 0x3A);
    private static final Color COR_TEXTO = new Color(0xF0, 0xF0, 0xF0);
    private static final Color COR_TEXTO_SEC = new Color(0xA0, 0xA0, 0xA0);
    private static final Color COR_ACENTO = new Color(0xC6, 0xFF, 0x00);
    private static final Color COR_ACENTO_HOVER = new Color(0xD4, 0xFF, 0x4D);
    private static final Color COR_LINHA_PAR = new Color(0x1E, 0x1E, 0x1E);
    private static final Color COR_LINHA_IMPAR = new Color(0x28, 0x28, 0x28);
    private static final Color COR_VERMELHO = new Color(0xFF, 0x45, 0x45);
    private static final Color COR_VERDE = new Color(0x4C, 0xAF, 0x50);
    private static final Color COR_AMARELO = new Color(0xFF, 0xD5, 0x4F);

    // -------------------------------------------------------------------------
    // Fonte padrao
    // -------------------------------------------------------------------------
    private static final Font FONTE_PADRAO = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_TABELA = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_HEADER = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONTE_STATUS = new Font("Segoe UI", Font.PLAIN, 12);

    // =========================================================================
    // Construtor — monta toda a interface
    // =========================================================================
    public JanelaPrincipal() {
        // Inicializar dados
        produtos = new Produto[50];
        totalProdutos = 0;
        categorias = new Categoria[10];
        totalCategorias = 0;
        preCadastrarDados();

        // Configuracoes da janela
        setTitle("Sistema de Gerenciamento de Estoque — AnuncioLab");
        setSize(1100, 680);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout(0, 0));

        // Montar areas
        add(criarBarraSuperior(), BorderLayout.NORTH);
        add(criarPainelCentral(), BorderLayout.CENTER);
        add(criarBarraStatus(), BorderLayout.SOUTH);

        // Preencher tabela
        atualizarTabela();
    }

    // =========================================================================
    // Pre-cadastra dados de exemplo (mesmos da versao console)
    // =========================================================================
    private void preCadastrarDados() {
        categorias[0] = new Categoria("Eletronicos", 0.13);
        categorias[1] = new Categoria("Moda", 0.16);
        categorias[2] = new Categoria("Casa e Cozinha", 0.12);
        totalCategorias = 3;

        produtos[0] = new Produto("ELT-001", "Fone Bluetooth JBL",
                "Eletronicos", 45.00, 89.90, 25, true);
        produtos[1] = new Produto("MOD-001", "Camiseta Basica Algodao",
                "Moda", 18.50, 49.90, 120, true);
        produtos[2] = new Produto("CSA-001", "Cafeteira Eletrica 220V",
                "Casa e Cozinha", 85.00, 179.90, 8, false);
        totalProdutos = 3;
    }

    // =========================================================================
    // Barra superior — titulo + botoes de acao
    // =========================================================================
    private JPanel criarBarraSuperior() {
        JPanel barra = new JPanel(new BorderLayout(16, 0));
        barra.setBackground(COR_PAINEL);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA),
                new EmptyBorder(12, 20, 12, 20)
        ));

        // Logo / titulo
        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelTitulo.setOpaque(false);

        JLabel icone = new JLabel("\u25CF");
        icone.setFont(new Font("Segoe UI", Font.BOLD, 28));
        icone.setForeground(COR_ACENTO);
        painelTitulo.add(icone);

        JLabel titulo = new JLabel("AnuncioLab");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TEXTO);
        painelTitulo.add(titulo);

        JLabel subtitulo = new JLabel("  |  Gerenciamento de Estoque");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(COR_TEXTO_SEC);
        painelTitulo.add(subtitulo);

        barra.add(painelTitulo, BorderLayout.WEST);

        // Botoes de acao
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        painelBotoes.setOpaque(false);

        JButton btnCadastrar = criarBotaoPrimario("+ Cadastrar Produto");
        btnCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirCadastro();
            }
        });

        JButton btnMovimentar = criarBotaoSecundario("Movimentar Estoque");
        btnMovimentar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                abrirMovimentacao();
            }
        });

        JButton btnAnuncio = criarBotaoSecundario("Ativar/Pausar");
        btnAnuncio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alternarAnuncioSelecionado();
            }
        });

        JButton btnAtualizar = criarBotaoSecundario("\u21BB Atualizar");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarTabela();
            }
        });

        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnMovimentar);
        painelBotoes.add(btnAnuncio);
        painelBotoes.add(btnAtualizar);

        barra.add(painelBotoes, BorderLayout.EAST);

        return barra;
    }

    // =========================================================================
    // Painel central — tabela de produtos
    // =========================================================================
    private JPanel criarPainelCentral() {
        JPanel painel = new JPanel(new BorderLayout());
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(12, 20, 4, 20));

        // Colunas da tabela
        String[] colunas = {
            "Cod", "SKU", "Nome", "Categoria", "Preco Venda (R$)",
            "Estoque", "Status", "Margem %"
        };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(modeloTabela);
        tabelaProdutos.setFont(FONTE_TABELA);
        tabelaProdutos.setRowHeight(36);
        tabelaProdutos.setShowGrid(false);
        tabelaProdutos.setIntercellSpacing(new Dimension(0, 0));
        tabelaProdutos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaProdutos.setSelectionBackground(new Color(0xC6, 0xFF, 0x00, 40));
        tabelaProdutos.setSelectionForeground(COR_TEXTO);
        tabelaProdutos.setForeground(COR_TEXTO);
        tabelaProdutos.setBackground(COR_FUNDO);
        tabelaProdutos.setFillsViewportHeight(true);

        // Header da tabela
        JTableHeader header = tabelaProdutos.getTableHeader();
        header.setFont(FONTE_HEADER);
        header.setBackground(COR_PAINEL);
        header.setForeground(COR_TEXTO_SEC);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, COR_ACENTO));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 40));

        // Renderizador customizado para linhas alternadas e status colorido
        tabelaProdutos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {

                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                // Fundo alternado
                if (!isSelected) {
                    if (row % 2 == 0) {
                        comp.setBackground(COR_LINHA_PAR);
                    } else {
                        comp.setBackground(COR_LINHA_IMPAR);
                    }
                }

                comp.setForeground(COR_TEXTO);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                // Coluna Status — cor especial
                if (col == 6 && value != null) {
                    String statusTexto = value.toString();
                    if (statusTexto.equals("ATIVO")) {
                        comp.setForeground(COR_VERDE);
                    } else {
                        comp.setForeground(COR_VERMELHO);
                    }
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                }

                // Coluna Estoque — alerta se baixo
                if (col == 5 && value != null) {
                    try {
                        int qtd = Integer.parseInt(value.toString());
                        if (qtd < 5) {
                            comp.setForeground(COR_AMARELO);
                            setFont(new Font("Segoe UI", Font.BOLD, 13));
                        }
                    } catch (NumberFormatException ex) {
                        // ignora
                    }
                }

                // Coluna Margem — cor baseada no valor
                if (col == 7 && value != null) {
                    try {
                        String margemStr = value.toString().replace("%", "").replace(",", ".");
                        double margem = Double.parseDouble(margemStr);
                        if (margem < 0) {
                            comp.setForeground(COR_VERMELHO);
                        } else if (margem > 50) {
                            comp.setForeground(COR_ACENTO);
                        }
                    } catch (NumberFormatException ex) {
                        // ignora
                    }
                }

                return comp;
            }
        });

        // Larguras das colunas
        TableColumnModel colModel = tabelaProdutos.getColumnModel();
        int[] larguras = {50, 90, 200, 120, 110, 70, 80, 80};
        for (int i = 0; i < larguras.length; i++) {
            colModel.getColumn(i).setPreferredWidth(larguras[i]);
        }

        JScrollPane scroll = new JScrollPane(tabelaProdutos);
        scroll.setBackground(COR_FUNDO);
        scroll.getViewport().setBackground(COR_FUNDO);
        scroll.setBorder(BorderFactory.createLineBorder(COR_BORDA, 1));

        // Remove scrollbar styling para ficar integrado
        scroll.getVerticalScrollBar().setBackground(COR_PAINEL);
        scroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            protected void configureScrollBarColors() {
                this.thumbColor = COR_BORDA;
                this.trackColor = COR_PAINEL;
            }
        });

        painel.add(scroll, BorderLayout.CENTER);
        return painel;
    }

    // =========================================================================
    // Barra de status inferior — totais do estoque
    // =========================================================================
    private JPanel criarBarraStatus() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(COR_PAINEL);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA),
                new EmptyBorder(10, 20, 10, 20)
        ));

        JPanel painelEsq = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        painelEsq.setOpaque(false);

        lblTotalProdutos = criarLabelStatus("Produtos: 0");
        lblValorEstoque = criarLabelStatus("Valor Estoque: R$ 0,00");
        lblPausados = criarLabelStatus("Pausados: 0");

        painelEsq.add(lblTotalProdutos);
        painelEsq.add(criarSeparadorVertical());
        painelEsq.add(lblValorEstoque);
        painelEsq.add(criarSeparadorVertical());
        painelEsq.add(lblPausados);

        barra.add(painelEsq, BorderLayout.WEST);

        lblStatus = new JLabel("Pronto");
        lblStatus.setFont(FONTE_STATUS);
        lblStatus.setForeground(COR_TEXTO_SEC);
        barra.add(lblStatus, BorderLayout.EAST);

        return barra;
    }

    // =========================================================================
    // Atualiza a tabela com os dados atuais do array de produtos
    // =========================================================================
    public void atualizarTabela() {
        // Limpar tabela
        modeloTabela.setRowCount(0);

        double valorTotal = 0;
        int pausados = 0;

        for (int i = 0; i < totalProdutos; i++) {
            Produto p = produtos[i];

            String status;
            if (p.isAnuncioAtivo()) {
                status = "ATIVO";
            } else {
                status = "PAUSADO";
                pausados++;
            }

            Object[] linha = {
                p.getCodigo(),
                p.getSku(),
                p.getNome(),
                p.getCategoria(),
                String.format("%.2f", p.getPrecoVenda()),
                p.getQuantidadeEstoque(),
                status,
                String.format("%.1f%%", p.calcularMargemLucro())
            };
            modeloTabela.addRow(linha);

            valorTotal = valorTotal + p.calcularValorTotalEstoque();
        }

        // Atualizar barra de status
        lblTotalProdutos.setText("Produtos: " + totalProdutos);
        lblValorEstoque.setText("Valor Estoque: R$ " + String.format("%.2f", valorTotal));
        lblPausados.setText("Pausados: " + pausados);

        lblStatus.setText("Atualizado");
    }

    // =========================================================================
    // Abre a janela de cadastro de produto
    // =========================================================================
    private void abrirCadastro() {
        JanelaCadastroProduto dialogo = new JanelaCadastroProduto(
                this, produtos, totalProdutos, categorias, totalCategorias);
        dialogo.setVisible(true);

        // Apos fechar, verificar se adicionou produto
        if (dialogo.isProdutoAdicionado()) {
            totalProdutos++;
            atualizarTabela();
            lblStatus.setText("Produto cadastrado com sucesso!");
        }
    }

    // =========================================================================
    // Abre a janela de movimentacao de estoque
    // =========================================================================
    private void abrirMovimentacao() {
        if (totalProdutos == 0) {
            mostrarErro("Nenhum produto cadastrado!");
            return;
        }

        JanelaMovimentacaoEstoque dialogo = new JanelaMovimentacaoEstoque(
                this, produtos, totalProdutos);
        dialogo.setVisible(true);

        atualizarTabela();
        if (dialogo.isOperacaoRealizada()) {
            lblStatus.setText("Movimentacao realizada com sucesso!");
        }
    }

    // =========================================================================
    // Alterna o status do anuncio do produto selecionado na tabela
    // =========================================================================
    private void alternarAnuncioSelecionado() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada < 0) {
            mostrarErro("Selecione um produto na tabela primeiro!");
            return;
        }

        Produto produto = produtos[linhaSelecionada];

        if (produto.isAnuncioAtivo()) {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Deseja PAUSAR o anuncio de \"" + produto.getNome() + "\"?",
                    "Pausar Anuncio", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                produto.pausarAnuncio();
                lblStatus.setText("Anuncio pausado: " + produto.getNome());
            }
        } else {
            int resposta = JOptionPane.showConfirmDialog(this,
                    "Deseja ATIVAR o anuncio de \"" + produto.getNome() + "\"?",
                    "Ativar Anuncio", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (resposta == JOptionPane.YES_OPTION) {
                produto.ativarAnuncio();
                lblStatus.setText("Anuncio ativado: " + produto.getNome());
            }
        }

        atualizarTabela();
    }

    // =========================================================================
    // Metodos auxiliares de UI
    // =========================================================================

    // Cria um botao primario (verde-lima)
    private JButton criarBotaoPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(Color.BLACK);
        btn.setBackground(COR_ACENTO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COR_ACENTO_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COR_ACENTO);
            }
        });

        return btn;
    }

    // Cria um botao secundario (fundo escuro, borda)
    private JButton criarBotaoSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOTAO);
        btn.setForeground(COR_TEXTO);
        btn.setBackground(COR_PAINEL);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(9, 16, 9, 16)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COR_BORDA);
                btn.setForeground(COR_ACENTO);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COR_PAINEL);
                btn.setForeground(COR_TEXTO);
            }
        });

        return btn;
    }

    // Label para a barra de status
    private JLabel criarLabelStatus(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FONTE_STATUS);
        lbl.setForeground(COR_TEXTO_SEC);
        return lbl;
    }

    // Separador vertical fino para a barra de status
    private JComponent criarSeparadorVertical() {
        JPanel sep = new JPanel();
        sep.setPreferredSize(new Dimension(1, 16));
        sep.setBackground(COR_BORDA);
        return sep;
    }

    // Exibe mensagem de erro
    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem,
                "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    // Getter para totalProdutos (usado pelas janelas filhas)
    public int getTotalProdutos() {
        return totalProdutos;
    }
}
