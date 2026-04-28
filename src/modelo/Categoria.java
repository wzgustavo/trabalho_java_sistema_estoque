// =============================================================================
// Classe Modelo: Categoria
// Representa uma categoria de produtos no marketplace.
// Demonstra: encapsulamento, construtores e calculo de comissao.
// =============================================================================
package modelo;

public class Categoria {

    // Atributos privados (encapsulamento)
    private int id;
    private String nome;
    private double comissaoMarketplace; // ex: 0.12 = 12%

    // Contador estatico para gerar IDs sequenciais
    private static int contadorId = 1;

    // Construtor padrao (sem parametros)
    public Categoria() {
        this.id = contadorId;
        contadorId++;
        this.nome = "";
        this.comissaoMarketplace = 0.0;
    }

    // Construtor com parametros
    public Categoria(String nome, double comissaoMarketplace) {
        this.id = contadorId;
        contadorId++;
        this.nome = nome;
        this.comissaoMarketplace = comissaoMarketplace;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getComissaoMarketplace() {
        return comissaoMarketplace;
    }

    public void setComissaoMarketplace(double comissaoMarketplace) {
        this.comissaoMarketplace = comissaoMarketplace;
    }

    // Calcula o valor da comissao sobre uma venda
    public double calcularComissao(double valorVenda) {
        return valorVenda * comissaoMarketplace;
    }

    // Retorna informacoes formatadas da categoria
    public String exibirInformacoes() {
        String info = "";
        info = info + "   ID.............: " + id + "\n";
        info = info + "   Nome...........: " + nome + "\n";
        info = info + "   Comissao.......: " + String.format("%.0f", comissaoMarketplace * 100) + "%";
        return info;
    }
}
