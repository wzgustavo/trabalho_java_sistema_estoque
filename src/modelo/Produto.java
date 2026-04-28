// =============================================================================
// Classe Modelo: Produto
// Representa um produto cadastrado no sistema de estoque do marketplace.
// Demonstra: encapsulamento (atributos private + getters/setters),
//            construtores (padrão e parametrizado), e métodos de negócio.
// =============================================================================
package modelo;

public class Produto {

    // -------------------------------------------------------------------------
    // Atributos privados (encapsulamento)
    // -------------------------------------------------------------------------
    private int codigo;
    private String sku;
    private String nome;
    private String categoria;
    private double precoCusto;
    private double precoVenda;
    private int quantidadeEstoque;
    private boolean anuncioAtivo;

    // Contador estático para gerar códigos sequenciais automaticamente
    private static int contadorCodigo = 1;

    // -------------------------------------------------------------------------
    // Construtor padrão (sem parâmetros)
    // Inicializa o produto com valores "vazios" e gera o código automaticamente
    // -------------------------------------------------------------------------
    public Produto() {
        this.codigo = contadorCodigo;
        contadorCodigo++;
        this.sku = "";
        this.nome = "";
        this.categoria = "";
        this.precoCusto = 0.0;
        this.precoVenda = 0.0;
        this.quantidadeEstoque = 0;
        this.anuncioAtivo = false;
    }

    // -------------------------------------------------------------------------
    // Construtor com parâmetros (código é gerado automaticamente)
    // Permite criar um produto já preenchido de uma vez
    // -------------------------------------------------------------------------
    public Produto(String sku, String nome, String categoria,
                   double precoCusto, double precoVenda,
                   int quantidadeEstoque, boolean anuncioAtivo) {
        this.codigo = contadorCodigo;
        contadorCodigo++;
        this.sku = sku;
        this.nome = nome;
        this.categoria = categoria;
        this.precoCusto = precoCusto;
        this.precoVenda = precoVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.anuncioAtivo = anuncioAtivo;
    }

    // -------------------------------------------------------------------------
    // Getters e Setters (acesso controlado aos atributos)
    // -------------------------------------------------------------------------

    public int getCodigo() {
        return codigo;
    }

    // Código não tem setter público pois é gerado automaticamente

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(double precoCusto) {
        this.precoCusto = precoCusto;
    }

    public double getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(double precoVenda) {
        this.precoVenda = precoVenda;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public boolean isAnuncioAtivo() {
        return anuncioAtivo;
    }

    public void setAnuncioAtivo(boolean anuncioAtivo) {
        this.anuncioAtivo = anuncioAtivo;
    }

    // -------------------------------------------------------------------------
    // Métodos de negócio
    // -------------------------------------------------------------------------

    /**
     * Calcula a margem de lucro percentual do produto.
     * Fórmula: ((precoVenda - precoCusto) / precoCusto) * 100
     * Retorna 0 se o preço de custo for zero (evita divisão por zero).
     */
    public double calcularMargemLucro() {
        if (precoCusto == 0) {
            return 0.0;
        }
        return ((precoVenda - precoCusto) / precoCusto) * 100;
    }

    /**
     * Calcula o valor total do estoque baseado no preço de custo.
     * Fórmula: precoCusto * quantidadeEstoque
     */
    public double calcularValorTotalEstoque() {
        return precoCusto * quantidadeEstoque;
    }

    /**
     * Dá entrada de produtos no estoque (recebimento de mercadoria).
     * Valida se a quantidade informada é maior que zero.
     */
    public void darEntradaEstoque(int quantidade) {
        if (quantidade > 0) {
            this.quantidadeEstoque = this.quantidadeEstoque + quantidade;
        }
    }

    /**
     * Dá saída de produtos do estoque (venda realizada).
     * Retorna true se a operação foi bem-sucedida, false se não houver
     * estoque suficiente.
     */
    public boolean darSaidaEstoque(int quantidade) {
        if (quantidade <= 0) {
            return false;
        }
        if (quantidade > this.quantidadeEstoque) {
            return false;
        }
        this.quantidadeEstoque = this.quantidadeEstoque - quantidade;
        return true;
    }

    /**
     * Ativa o anúncio do produto no marketplace.
     */
    public void ativarAnuncio() {
        this.anuncioAtivo = true;
    }

    /**
     * Pausa o anúncio do produto no marketplace.
     */
    public void pausarAnuncio() {
        this.anuncioAtivo = false;
    }

    /**
     * Retorna uma String formatada com todas as informações do produto.
     * Usado para exibir os dados de forma organizada no console.
     */
    public String exibirInformacoes() {
        String status;
        if (anuncioAtivo) {
            status = "ATIVO";
        } else {
            status = "PAUSADO";
        }

        String info = "";
        info = info + "   Codigo.........: " + codigo + "\n";
        info = info + "   SKU............: " + sku + "\n";
        info = info + "   Nome...........: " + nome + "\n";
        info = info + "   Categoria......: " + categoria + "\n";
        info = info + "   Preco Custo....: R$ " + String.format("%.2f", precoCusto) + "\n";
        info = info + "   Preco Venda....: R$ " + String.format("%.2f", precoVenda) + "\n";
        info = info + "   Margem Lucro...: " + String.format("%.1f", calcularMargemLucro()) + "%\n";
        info = info + "   Qtd Estoque....: " + quantidadeEstoque + " unidades\n";
        info = info + "   Valor Estoque..: R$ " + String.format("%.2f", calcularValorTotalEstoque()) + "\n";
        info = info + "   Anuncio........: " + status;

        return info;
    }
}
