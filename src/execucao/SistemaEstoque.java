// =============================================================================
// Classe de Execucao: SistemaEstoque
// Contem o metodo main e toda a logica de interacao com o usuario via console.
// Utiliza: Scanner, array fixo, switch, if/else, for, do-while, try/catch.
// =============================================================================
package execucao;

import java.util.Scanner;
import modelo.Produto;
import modelo.Categoria;

public class SistemaEstoque {

    // Array fixo de produtos (sem ArrayList) e controle de quantidade
    static Produto[] produtos = new Produto[50];
    static int totalProdutos = 0;

    // Array de categorias para demonstrar uso de multiplas classes modelo
    static Categoria[] categorias = new Categoria[10];
    static int totalCategorias = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Pre-cadastro de categorias de exemplo
        categorias[0] = new Categoria("Eletronicos", 0.13);
        categorias[1] = new Categoria("Moda", 0.16);
        categorias[2] = new Categoria("Casa e Cozinha", 0.12);
        totalCategorias = 3;

        // Pre-cadastro de 3 produtos de exemplo
        produtos[0] = new Produto("ELT-001", "Fone Bluetooth JBL",
                "Eletronicos", 45.00, 89.90, 25, true);
        produtos[1] = new Produto("MOD-001", "Camiseta Basica Algodao",
                "Moda", 18.50, 49.90, 120, true);
        produtos[2] = new Produto("CSA-001", "Cafeteira Eletrica 220V",
                "Casa e Cozinha", 85.00, 179.90, 8, false);
        totalProdutos = 3;

        int opcao;

        // Loop principal do sistema (do-while)
        do {
            exibirMenu();
            opcao = lerInteiro(scanner);

            // Tratamento das opcoes usando switch
            switch (opcao) {
                case 1:
                    cadastrarProduto(scanner);
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    darEntradaEstoque(scanner);
                    break;
                case 4:
                    darSaidaEstoque(scanner);
                    break;
                case 5:
                    alternarAnuncio(scanner);
                    break;
                case 6:
                    buscarPorSku(scanner);
                    break;
                case 7:
                    exibirRelatorio();
                    break;
                case 8:
                    editarPrecoVenda(scanner);
                    break;
                case 0:
                    System.out.println("\n   Encerrando o sistema...");
                    System.out.println("   Obrigado por usar o SistemaGerenciamentoEstoque!");
                    break;
                default:
                    System.out.println("\n   [ERRO] Opcao invalida! Tente novamente.");
                    break;
            }
        } while (opcao != 0);

        scanner.close();
    }

    // =========================================================================
    // Exibe o menu principal do sistema
    // =========================================================================
    public static void exibirMenu() {
        System.out.println("\n============================================================");
        System.out.println("      SISTEMA DE GERENCIAMENTO DE ESTOQUE - MARKETPLACE     ");
        System.out.println("============================================================");
        System.out.println("   [1] Cadastrar novo produto");
        System.out.println("   [2] Listar todos os produtos");
        System.out.println("   [3] Dar entrada no estoque");
        System.out.println("   [4] Dar saida no estoque (venda)");
        System.out.println("   [5] Ativar/Pausar anuncio");
        System.out.println("   [6] Buscar produto por SKU");
        System.out.println("   [7] Relatorio geral");
        System.out.println("   [8] Editar preco de venda");
        System.out.println("   [0] Sair");
        System.out.println("------------------------------------------------------------");
        System.out.print("   Escolha uma opcao: ");
    }

    // =========================================================================
    // Le um numero inteiro do usuario, tratando entrada invalida com try/catch
    // =========================================================================
    public static int lerInteiro(Scanner scanner) {
        try {
            int valor = Integer.parseInt(scanner.nextLine().trim());
            return valor;
        } catch (NumberFormatException e) {
            return -1; // Retorna -1 para indicar entrada invalida
        }
    }

    // =========================================================================
    // Le um numero double do usuario, tratando entrada invalida
    // =========================================================================
    public static double lerDouble(Scanner scanner) {
        try {
            String entrada = scanner.nextLine().trim();
            entrada = entrada.replace(",", "."); // Aceita virgula ou ponto
            double valor = Double.parseDouble(entrada);
            return valor;
        } catch (NumberFormatException e) {
            return -1.0; // Retorna -1 para indicar erro
        }
    }

    // =========================================================================
    // Opcao 1: Cadastrar novo produto
    // Valida: SKU nao vazio, SKU nao duplicado, precos nao negativos,
    //         avisa se preco de venda < preco de custo
    // =========================================================================
    public static void cadastrarProduto(Scanner scanner) {
        System.out.println("\n--- CADASTRAR NOVO PRODUTO ---");

        // Verificar se o array esta cheio
        if (totalProdutos >= 50) {
            System.out.println("   [ERRO] Limite de 50 produtos atingido!");
            return;
        }

        // Ler SKU
        System.out.print("   SKU (ex: ELT-002): ");
        String sku = scanner.nextLine().trim();
        if (sku.isEmpty()) {
            System.out.println("   [ERRO] O SKU nao pode estar vazio!");
            return;
        }

        // Verificar SKU duplicado usando for
        for (int i = 0; i < totalProdutos; i++) {
            if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                System.out.println("   [ERRO] Ja existe um produto com o SKU: " + sku);
                return;
            }
        }

        // Ler nome
        System.out.print("   Nome do produto: ");
        String nome = scanner.nextLine().trim();
        if (nome.isEmpty()) {
            System.out.println("   [ERRO] O nome nao pode estar vazio!");
            return;
        }

        // Ler categoria
        System.out.println("   Categorias disponiveis:");
        for (int i = 0; i < totalCategorias; i++) {
            System.out.println("      " + (i + 1) + ". " + categorias[i].getNome());
        }
        System.out.print("   Escolha a categoria (numero) ou digite o nome: ");
        String entradaCategoria = scanner.nextLine().trim();
        String categoriaProduto;

        // Tenta interpretar como numero primeiro, senao usa como texto
        try {
            int numCat = Integer.parseInt(entradaCategoria);
            if (numCat >= 1 && numCat <= totalCategorias) {
                categoriaProduto = categorias[numCat - 1].getNome();
            } else {
                categoriaProduto = entradaCategoria;
            }
        } catch (NumberFormatException e) {
            categoriaProduto = entradaCategoria;
        }

        // Ler preco de custo
        System.out.print("   Preco de custo (R$): ");
        double precoCusto = lerDouble(scanner);
        if (precoCusto < 0) {
            System.out.println("   [ERRO] Preco de custo nao pode ser negativo!");
            return;
        }

        // Ler preco de venda
        System.out.print("   Preco de venda (R$): ");
        double precoVenda = lerDouble(scanner);
        if (precoVenda < 0) {
            System.out.println("   [ERRO] Preco de venda nao pode ser negativo!");
            return;
        }

        // Aviso se preco de venda for menor que custo
        if (precoVenda < precoCusto) {
            System.out.println("   [AVISO] O preco de venda esta abaixo do custo!");
            System.out.print("   Deseja continuar mesmo assim? (S/N): ");
            String confirmacao = scanner.nextLine().trim();
            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("   Cadastro cancelado.");
                return;
            }
        }

        // Ler quantidade em estoque
        System.out.print("   Quantidade em estoque: ");
        int quantidade = lerInteiro(scanner);
        if (quantidade < 0) {
            System.out.println("   [ERRO] Quantidade nao pode ser negativa!");
            return;
        }

        // Ativar anuncio?
        System.out.print("   Ativar anuncio no marketplace? (S/N): ");
        String ativar = scanner.nextLine().trim();
        boolean anuncioAtivo = ativar.equalsIgnoreCase("S");

        // Criar o objeto Produto usando o construtor com parametros
        Produto novoProduto = new Produto(sku, nome, categoriaProduto,
                precoCusto, precoVenda, quantidade, anuncioAtivo);

        // Adicionar ao array
        produtos[totalProdutos] = novoProduto;
        totalProdutos++;

        System.out.println("\n   Produto cadastrado com sucesso! Codigo: " + novoProduto.getCodigo());
    }

    // =========================================================================
    // Opcao 2: Listar todos os produtos cadastrados
    // Usa for para percorrer o array
    // =========================================================================
    public static void listarProdutos() {
        System.out.println("\n--- LISTA DE PRODUTOS CADASTRADOS ---");

        if (totalProdutos == 0) {
            System.out.println("   Nenhum produto cadastrado.");
            return;
        }

        for (int i = 0; i < totalProdutos; i++) {
            System.out.println("------------------------------------------------------------");
            System.out.println(produtos[i].exibirInformacoes());
        }
        System.out.println("------------------------------------------------------------");
        System.out.println("   Total de produtos: " + totalProdutos);
    }

    // =========================================================================
    // Opcao 3: Dar entrada no estoque de um produto
    // =========================================================================
    public static void darEntradaEstoque(Scanner scanner) {
        System.out.println("\n--- ENTRADA DE ESTOQUE ---");

        System.out.print("   Digite o SKU do produto: ");
        String sku = scanner.nextLine().trim();

        // Buscar produto pelo SKU usando for
        Produto produto = null;
        for (int i = 0; i < totalProdutos; i++) {
            if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                produto = produtos[i];
                break;
            }
        }

        // Validar se encontrou
        if (produto == null) {
            System.out.println("   [ERRO] Produto nao encontrado com SKU: " + sku);
            return;
        }

        System.out.println("   Produto: " + produto.getNome());
        System.out.println("   Estoque atual: " + produto.getQuantidadeEstoque());
        System.out.print("   Quantidade a dar entrada: ");
        int quantidade = lerInteiro(scanner);

        if (quantidade <= 0) {
            System.out.println("   [ERRO] Quantidade deve ser maior que zero!");
            return;
        }

        produto.darEntradaEstoque(quantidade);
        System.out.println("   Entrada realizada! Novo estoque: " + produto.getQuantidadeEstoque());
    }

    // =========================================================================
    // Opcao 4: Dar saida no estoque (simular venda)
    // Verifica se o anuncio esta ativo antes de permitir a venda
    // =========================================================================
    public static void darSaidaEstoque(Scanner scanner) {
        System.out.println("\n--- SAIDA DE ESTOQUE (VENDA) ---");

        System.out.print("   Digite o SKU do produto: ");
        String sku = scanner.nextLine().trim();

        Produto produto = null;
        for (int i = 0; i < totalProdutos; i++) {
            if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                produto = produtos[i];
                break;
            }
        }

        if (produto == null) {
            System.out.println("   [ERRO] Produto nao encontrado com SKU: " + sku);
            return;
        }

        // Avisar se o anuncio esta pausado
        if (!produto.isAnuncioAtivo()) {
            System.out.println("   [AVISO] O anuncio deste produto esta PAUSADO!");
            System.out.println("   Em um marketplace real, este produto nao estaria visivel.");
            System.out.print("   Deseja continuar com a saida mesmo assim? (S/N): ");
            String continuar = scanner.nextLine().trim();
            if (!continuar.equalsIgnoreCase("S")) {
                System.out.println("   Operacao cancelada.");
                return;
            }
        }

        System.out.println("   Produto: " + produto.getNome());
        System.out.println("   Estoque atual: " + produto.getQuantidadeEstoque());
        System.out.print("   Quantidade a retirar: ");
        int quantidade = lerInteiro(scanner);

        if (quantidade <= 0) {
            System.out.println("   [ERRO] Quantidade deve ser maior que zero!");
            return;
        }

        boolean sucesso = produto.darSaidaEstoque(quantidade);
        if (sucesso) {
            System.out.println("   Saida realizada! Novo estoque: " + produto.getQuantidadeEstoque());
            double valorVenda = produto.getPrecoVenda() * quantidade;
            System.out.println("   Valor total da venda: R$ " + String.format("%.2f", valorVenda));

            // Usar a classe Categoria para calcular comissao
            for (int i = 0; i < totalCategorias; i++) {
                if (categorias[i].getNome().equalsIgnoreCase(produto.getCategoria())) {
                    double comissao = categorias[i].calcularComissao(valorVenda);
                    System.out.println("   Comissao marketplace ("
                        + String.format("%.0f", categorias[i].getComissaoMarketplace() * 100)
                        + "%): R$ " + String.format("%.2f", comissao));
                    System.out.println("   Receita liquida: R$ "
                        + String.format("%.2f", valorVenda - comissao));
                    break;
                }
            }
        } else {
            System.out.println("   [ERRO] Estoque insuficiente! Disponivel: " + produto.getQuantidadeEstoque());
        }
    }

    // =========================================================================
    // Opcao 5: Ativar ou pausar o anuncio de um produto
    // =========================================================================
    public static void alternarAnuncio(Scanner scanner) {
        System.out.println("\n--- ATIVAR / PAUSAR ANUNCIO ---");

        System.out.print("   Digite o SKU do produto: ");
        String sku = scanner.nextLine().trim();

        Produto produto = null;
        for (int i = 0; i < totalProdutos; i++) {
            if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                produto = produtos[i];
                break;
            }
        }

        if (produto == null) {
            System.out.println("   [ERRO] Produto nao encontrado com SKU: " + sku);
            return;
        }

        System.out.println("   Produto: " + produto.getNome());

        if (produto.isAnuncioAtivo()) {
            System.out.println("   Status atual: ATIVO");
            System.out.print("   Deseja PAUSAR o anuncio? (S/N): ");
            String resposta = scanner.nextLine().trim();
            if (resposta.equalsIgnoreCase("S")) {
                produto.pausarAnuncio();
                System.out.println("   Anuncio PAUSADO para: " + produto.getNome());
            }
        } else {
            System.out.println("   Status atual: PAUSADO");
            System.out.print("   Deseja ATIVAR o anuncio? (S/N): ");
            String resposta = scanner.nextLine().trim();
            if (resposta.equalsIgnoreCase("S")) {
                produto.ativarAnuncio();
                System.out.println("   Anuncio ATIVADO para: " + produto.getNome());
            }
        }
    }

    // =========================================================================
    // Opcao 6: Buscar produto pelo SKU
    // =========================================================================
    public static void buscarPorSku(Scanner scanner) {
        System.out.println("\n--- BUSCAR PRODUTO POR SKU ---");

        System.out.print("   Digite o SKU: ");
        String sku = scanner.nextLine().trim();

        boolean encontrado = false;
        for (int i = 0; i < totalProdutos; i++) {
            if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                System.out.println("------------------------------------------------------------");
                System.out.println(produtos[i].exibirInformacoes());
                System.out.println("------------------------------------------------------------");
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("   Nenhum produto encontrado com o SKU: " + sku);
        }
    }

    // =========================================================================
    // Opcao 7: Relatorio geral do estoque
    // Mostra: total de produtos, valor total do estoque, estoque baixo, pausados
    // =========================================================================
    public static void exibirRelatorio() {
        System.out.println("\n============================================================");
        System.out.println("                    RELATORIO GERAL                         ");
        System.out.println("============================================================");

        if (totalProdutos == 0) {
            System.out.println("   Nenhum produto cadastrado.");
            return;
        }

        double valorTotalEstoque = 0;
        int produtosAtivos = 0;
        int produtosPausados = 0;
        int produtosEstoqueBaixo = 0;

        // Calcular totais usando for e operadores aritmeticos
        for (int i = 0; i < totalProdutos; i++) {
            valorTotalEstoque = valorTotalEstoque + produtos[i].calcularValorTotalEstoque();

            if (produtos[i].isAnuncioAtivo()) {
                produtosAtivos++;
            } else {
                produtosPausados++;
            }

            if (produtos[i].getQuantidadeEstoque() < 5) {
                produtosEstoqueBaixo++;
            }
        }

        System.out.println("   Total de produtos cadastrados.: " + totalProdutos);
        System.out.println("   Valor total do estoque........: R$ " + String.format("%.2f", valorTotalEstoque));
        System.out.println("   Anuncios ativos...............: " + produtosAtivos);
        System.out.println("   Anuncios pausados.............: " + produtosPausados);
        System.out.println("   Produtos com estoque baixo....: " + produtosEstoqueBaixo);

        // Listar produtos com estoque baixo (< 5 unidades)
        if (produtosEstoqueBaixo > 0) {
            System.out.println("\n   ** ALERTA: Produtos com estoque baixo (< 5 unidades) **");
            for (int i = 0; i < totalProdutos; i++) {
                if (produtos[i].getQuantidadeEstoque() < 5) {
                    System.out.println("      - [" + produtos[i].getSku() + "] "
                        + produtos[i].getNome()
                        + " | Estoque: " + produtos[i].getQuantidadeEstoque());
                }
            }
        }

        // Listar produtos pausados
        if (produtosPausados > 0) {
            System.out.println("\n   ** Produtos com anuncio pausado **");
            for (int i = 0; i < totalProdutos; i++) {
                if (!produtos[i].isAnuncioAtivo()) {
                    System.out.println("      - [" + produtos[i].getSku() + "] "
                        + produtos[i].getNome());
                }
            }
        }

        // Exibir categorias cadastradas
        System.out.println("\n   ** Categorias do marketplace **");
        for (int i = 0; i < totalCategorias; i++) {
            System.out.println("      - " + categorias[i].getNome()
                + " (comissao: " + String.format("%.0f", categorias[i].getComissaoMarketplace() * 100) + "%)");
        }

        System.out.println("============================================================");
    }

    // =========================================================================
    // Opcao 8: Editar preco de venda de um produto
    // =========================================================================
    public static void editarPrecoVenda(Scanner scanner) {
        System.out.println("\n--- EDITAR PRECO DE VENDA ---");

        System.out.print("   Digite o SKU do produto: ");
        String sku = scanner.nextLine().trim();

        Produto produto = null;
        for (int i = 0; i < totalProdutos; i++) {
            if (produtos[i].getSku().equalsIgnoreCase(sku)) {
                produto = produtos[i];
                break;
            }
        }

        if (produto == null) {
            System.out.println("   [ERRO] Produto nao encontrado com SKU: " + sku);
            return;
        }

        System.out.println("   Produto: " + produto.getNome());
        System.out.println("   Preco de custo.: R$ " + String.format("%.2f", produto.getPrecoCusto()));
        System.out.println("   Preco atual.....: R$ " + String.format("%.2f", produto.getPrecoVenda()));
        System.out.println("   Margem atual....: " + String.format("%.1f", produto.calcularMargemLucro()) + "%");
        System.out.print("\n   Novo preco de venda (R$): ");
        double novoPreco = lerDouble(scanner);

        if (novoPreco < 0) {
            System.out.println("   [ERRO] Preco nao pode ser negativo!");
            return;
        }

        // Avisar se novo preco fica abaixo do custo
        if (novoPreco < produto.getPrecoCusto()) {
            System.out.println("   [AVISO] O novo preco esta abaixo do custo!");
            System.out.print("   Confirmar alteracao? (S/N): ");
            String confirma = scanner.nextLine().trim();
            if (!confirma.equalsIgnoreCase("S")) {
                System.out.println("   Alteracao cancelada.");
                return;
            }
        }

        produto.setPrecoVenda(novoPreco);
        System.out.println("   Preco atualizado com sucesso!");
        System.out.println("   Nova margem de lucro: " + String.format("%.1f", produto.calcularMargemLucro()) + "%");
    }
}
