// =============================================================================
// Janela de Cadastro de Produto (JDialog modal)
// Formulario para cadastrar um novo produto com validacoes visuais.
// IMPORTA classes de modelo/ — modelo/ NAO importa nada daqui.
// =============================================================================
package interface_grafica;

import modelo.Produto;
import modelo.Categoria;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class JanelaCadastroProduto extends JDialog {

    // Referencia ao array de produtos (compartilhado com JanelaPrincipal)
    private Produto[] produtos;
    private int totalProdutos;
    private Categoria[] categorias;
    private int totalCategorias;
    private boolean produtoAdicionado;

    // Campos do formulario
    private JTextField campoSku;
    private JTextField campoNome;
    private JComboBox<String> comboCategoria;
    private JTextField campoPrecoCusto;
    private JTextField campoPrecoVenda;
    private JTextField campoQuantidade;
    private JCheckBox checkAnuncio;

    // Labels de erro
    private JLabel erroSku;
    private JLabel erroNome;
    private JLabel erroPrecoCusto;
    private JLabel erroPrecoVenda;
    private JLabel erroQuantidade;

    // Cores (mesma paleta da JanelaPrincipal)
    private static final Color COR_FUNDO = new Color(0x1A, 0x1A, 0x1A);
    private static final Color COR_PAINEL = new Color(0x25, 0x25, 0x25);
    private static final Color COR_BORDA = new Color(0x3A, 0x3A, 0x3A);
    private static final Color COR_TEXTO = new Color(0xF0, 0xF0, 0xF0);
    private static final Color COR_TEXTO_SEC = new Color(0xA0, 0xA0, 0xA0);
    private static final Color COR_ACENTO = new Color(0xC6, 0xFF, 0x00);
    private static final Color COR_ACENTO_HOVER = new Color(0xD4, 0xFF, 0x4D);
    private static final Color COR_VERMELHO = new Color(0xFF, 0x45, 0x45);

    // Cores para campos de entrada — fundo claro com texto escuro para legibilidade
    private static final Color COR_CAMPO_FUNDO = Color.WHITE;
    private static final Color COR_CAMPO_TEXTO = Color.BLACK;
    private static final Color COR_CAMPO_CARET = Color.BLACK;

    private static final Font FONTE_LABEL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONTE_CAMPO = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONTE_BOTAO = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font FONTE_TITULO = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONTE_ERRO = new Font("Segoe UI", Font.PLAIN, 11);

    // =========================================================================
    // Construtor
    // =========================================================================
    public JanelaCadastroProduto(JFrame pai, Produto[] produtos, int totalProdutos,
                                  Categoria[] categorias, int totalCategorias) {
        super(pai, "Cadastrar Novo Produto", true); // true = modal
        this.produtos = produtos;
        this.totalProdutos = totalProdutos;
        this.categorias = categorias;
        this.totalCategorias = totalCategorias;
        this.produtoAdicionado = false;

        setSize(480, 620);
        setLocationRelativeTo(pai);
        setResizable(false);
        getContentPane().setBackground(COR_FUNDO);
        setLayout(new BorderLayout());

        add(criarHeader(), BorderLayout.NORTH);
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarRodape(), BorderLayout.SOUTH);
    }

    // =========================================================================
    // Header do dialogo
    // =========================================================================
    private JPanel criarHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        header.setBackground(COR_PAINEL);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, COR_BORDA),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel icone = new JLabel("\u2795");
        icone.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        icone.setForeground(COR_ACENTO);
        header.add(icone);

        JLabel titulo = new JLabel("Novo Produto");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_TEXTO);
        header.add(titulo);

        return header;
    }

    // =========================================================================
    // Formulario com os campos
    // =========================================================================
    private JPanel criarFormulario() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(COR_FUNDO);
        form.setBorder(new EmptyBorder(16, 24, 8, 24));

        // SKU
        campoSku = criarCampoTexto();
        erroSku = criarLabelErro();
        form.add(criarGrupoCampo("SKU *", campoSku, erroSku));

        // Nome
        campoNome = criarCampoTexto();
        erroNome = criarLabelErro();
        form.add(criarGrupoCampo("Nome do Produto *", campoNome, erroNome));

        // Categoria (combo box)
        comboCategoria = new JComboBox<String>();
        comboCategoria.setFont(FONTE_CAMPO);
        comboCategoria.setBackground(COR_CAMPO_FUNDO);
        comboCategoria.setForeground(COR_CAMPO_TEXTO);
        estilizarComboBox(comboCategoria);
        for (int i = 0; i < totalCategorias; i++) {
            comboCategoria.addItem(categorias[i].getNome());
        }
        comboCategoria.addItem("Outra...");
        JPanel grupoCategoria = criarGrupoCampoCombo("Categoria", comboCategoria);
        form.add(grupoCategoria);

        // Preco de Custo
        campoPrecoCusto = criarCampoTexto();
        erroPrecoCusto = criarLabelErro();
        form.add(criarGrupoCampo("Preco de Custo (R$) *", campoPrecoCusto, erroPrecoCusto));

        // Preco de Venda
        campoPrecoVenda = criarCampoTexto();
        erroPrecoVenda = criarLabelErro();
        form.add(criarGrupoCampo("Preco de Venda (R$) *", campoPrecoVenda, erroPrecoVenda));

        // Quantidade
        campoQuantidade = criarCampoTexto();
        erroQuantidade = criarLabelErro();
        form.add(criarGrupoCampo("Quantidade em Estoque *", campoQuantidade, erroQuantidade));

        // Anuncio ativo
        checkAnuncio = new JCheckBox("Ativar anuncio no marketplace");
        checkAnuncio.setFont(FONTE_LABEL);
        checkAnuncio.setForeground(COR_TEXTO);
        checkAnuncio.setBackground(COR_FUNDO);
        checkAnuncio.setFocusPainted(false);
        checkAnuncio.setSelected(true);
        JPanel painelCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
        painelCheck.setBackground(COR_FUNDO);
        painelCheck.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        painelCheck.add(checkAnuncio);
        form.add(painelCheck);

        return form;
    }

    // =========================================================================
    // Rodape com botoes Salvar e Cancelar
    // =========================================================================
    private JPanel criarRodape() {
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rodape.setBackground(COR_PAINEL);
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, COR_BORDA),
                new EmptyBorder(12, 16, 12, 16)
        ));

        // Botao Cancelar
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

        // Botao Salvar
        JButton btnSalvar = new JButton("Salvar Produto");
        btnSalvar.setFont(FONTE_BOTAO);
        btnSalvar.setForeground(Color.BLACK);
        btnSalvar.setBackground(COR_ACENTO);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setBorder(new EmptyBorder(10, 28, 10, 28));
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setOpaque(true);
        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarProduto();
            }
        });
        btnSalvar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnSalvar.setBackground(COR_ACENTO_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btnSalvar.setBackground(COR_ACENTO);
            }
        });

        rodape.add(btnCancelar);
        rodape.add(btnSalvar);

        return rodape;
    }

    // =========================================================================
    // Logica de salvar — valida campos e cria o Produto
    // =========================================================================
    private void salvarProduto() {
        // Limpar erros anteriores
        erroSku.setText(" ");
        erroNome.setText(" ");
        erroPrecoCusto.setText(" ");
        erroPrecoVenda.setText(" ");
        erroQuantidade.setText(" ");
        boolean temErro = false;

        // Validar SKU
        String sku = campoSku.getText().trim();
        if (sku.isEmpty()) {
            erroSku.setText("SKU e obrigatorio");
            temErro = true;
        } else {
            // Verificar duplicado
            for (int i = 0; i < totalProdutos; i++) {
                if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                    erroSku.setText("Ja existe um produto com este SKU");
                    temErro = true;
                    break;
                }
            }
        }

        // Validar Nome
        String nome = campoNome.getText().trim();
        if (nome.isEmpty()) {
            erroNome.setText("Nome e obrigatorio");
            temErro = true;
        }

        // Categoria
        String categoriaSelecionada = (String) comboCategoria.getSelectedItem();
        if (categoriaSelecionada.equals("Outra...")) {
            categoriaSelecionada = JOptionPane.showInputDialog(this,
                    "Digite o nome da categoria:", "Nova Categoria",
                    JOptionPane.PLAIN_MESSAGE);
            if (categoriaSelecionada == null || categoriaSelecionada.trim().isEmpty()) {
                categoriaSelecionada = "Sem Categoria";
            }
        }

        // Validar Preco de Custo
        double precoCusto = -1;
        try {
            String txtCusto = campoPrecoCusto.getText().trim().replace(",", ".");
            precoCusto = Double.parseDouble(txtCusto);
            if (precoCusto < 0) {
                erroPrecoCusto.setText("Preco nao pode ser negativo");
                temErro = true;
            }
        } catch (NumberFormatException ex) {
            erroPrecoCusto.setText("Valor invalido");
            temErro = true;
        }

        // Validar Preco de Venda
        double precoVenda = -1;
        try {
            String txtVenda = campoPrecoVenda.getText().trim().replace(",", ".");
            precoVenda = Double.parseDouble(txtVenda);
            if (precoVenda < 0) {
                erroPrecoVenda.setText("Preco nao pode ser negativo");
                temErro = true;
            }
        } catch (NumberFormatException ex) {
            erroPrecoVenda.setText("Valor invalido");
            temErro = true;
        }

        // Validar Quantidade
        int quantidade = -1;
        try {
            quantidade = Integer.parseInt(campoQuantidade.getText().trim());
            if (quantidade < 0) {
                erroQuantidade.setText("Quantidade nao pode ser negativa");
                temErro = true;
            }
        } catch (NumberFormatException ex) {
            erroQuantidade.setText("Valor invalido");
            temErro = true;
        }

        // Se houver erros, nao prossegue
        if (temErro) {
            return;
        }

        // Warning: venda < custo
        if (precoVenda < precoCusto) {
            int resp = JOptionPane.showConfirmDialog(this,
                    "O preco de venda (R$ " + String.format("%.2f", precoVenda) +
                    ") esta abaixo do custo (R$ " + String.format("%.2f", precoCusto) +
                    ").\nDeseja continuar?",
                    "Aviso de Margem Negativa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (resp != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // Verificar capacidade do array
        if (totalProdutos >= 50) {
            JOptionPane.showMessageDialog(this,
                    "Limite de 50 produtos atingido!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Criar produto usando construtor parametrizado da classe modelo
        Produto novoProduto = new Produto(sku, nome, categoriaSelecionada,
                precoCusto, precoVenda, quantidade, checkAnuncio.isSelected());

        produtos[totalProdutos] = novoProduto;
        produtoAdicionado = true;

        dispose();
    }

    // =========================================================================
    // Metodo para JanelaPrincipal verificar se adicionou produto
    // =========================================================================
    public boolean isProdutoAdicionado() {
        return produtoAdicionado;
    }

    // =========================================================================
    // Metodos auxiliares de criacao de componentes
    // =========================================================================

    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(FONTE_CAMPO);
        campo.setForeground(COR_CAMPO_TEXTO);
        campo.setBackground(COR_CAMPO_FUNDO);
        campo.setCaretColor(COR_CAMPO_CARET);
        campo.setOpaque(true);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return campo;
    }

    // Aplica estilo padronizado ao combo box — texto preto sobre fundo branco,
    // com hover verde-lima nos itens da lista dropdown
    private void estilizarComboBox(JComboBox<String> combo) {
        combo.setOpaque(true);
        combo.setRenderer(new DefaultListCellRenderer() {
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
    }

    private JLabel criarLabelErro() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(FONTE_ERRO);
        lbl.setForeground(COR_VERMELHO);
        return lbl;
    }

    private JPanel criarGrupoCampo(String labelTexto, JTextField campo, JLabel erro) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setBackground(COR_FUNDO);
        grupo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));
        grupo.setBorder(new EmptyBorder(0, 0, 2, 0));

        JLabel label = new JLabel(labelTexto);
        label.setFont(FONTE_LABEL);
        label.setForeground(COR_TEXTO_SEC);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupo.add(label);
        grupo.add(Box.createVerticalStrut(4));

        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupo.add(campo);

        erro.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupo.add(erro);

        return grupo;
    }

    private JPanel criarGrupoCampoCombo(String labelTexto, JComboBox<String> combo) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setBackground(COR_FUNDO);
        grupo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        grupo.setBorder(new EmptyBorder(0, 0, 6, 0));

        JLabel label = new JLabel(labelTexto);
        label.setFont(FONTE_LABEL);
        label.setForeground(COR_TEXTO_SEC);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        grupo.add(label);
        grupo.add(Box.createVerticalStrut(4));

        combo.setAlignmentX(Component.LEFT_ALIGNMENT);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        grupo.add(combo);

        return grupo;
    }
}
