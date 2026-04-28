# Sistema de Gerenciamento de Estoque — AnuncioLab

**Disciplina:** Programação Orientada a Objetos (POO)  
**Instituição:** Uniube  
**Alunos:**Gustavo Henrique Souza de Melo, Eduardo Fernandes de Lima, Gabriel Garcia
**RA's:** 5173139, 5173425, 5172506

---

## 📋 Descrição

Sistema de gerenciamento de catálogo e estoque inspirado em marketplaces como Mercado Livre. O projeto possui **duas camadas independentes** que compartilham as mesmas classes modelo:

- **Camada Console** (`execucao/`): versão com menu interativo no terminal — **apresentada ao professor**
- **Camada GUI** (`interface_grafica/`): interface gráfica Swing com tema escuro — **extra para demonstrar criatividade**

---

## 📁 Estrutura do Projeto

```
SistemaGerenciamentoEstoque/
├── src/
│   ├── modelo/                           ← CLASSES MODELO
│   │   ├── Produto.java
│   │   └── Categoria.java
│   ├── execucao/                         ← CLASSE DE EXECUÇÃO
│   │   └── SistemaEstoque.java
│   └── interface_grafica/                ← INTERFACE GRÁFICA (extra)
│       ├── JanelaPrincipal.java
│       ├── JanelaCadastroProduto.java
│       ├── JanelaMovimentacaoEstoque.java
│       └── AppGrafico.java
├── bin/                          
└── README.md
```

**Regra de dependência:** `interface_grafica/` importa `modelo/`, mas `modelo/` e `execucao/` **nunca** importam `interface_grafica/`.

---

## 🔧 Como Compilar

Abra o terminal na pasta raiz do projeto (`SistemaGerenciamentoEstoque/`) e execute:

```bash
javac -d bin src/modelo/*.java src/execucao/*.java src/interface_grafica/*.java
```

Isso compila tudo e coloca os `.class` na pasta `bin/`.

---

## ▶️ Como Executar

### Versão CONSOLE (a que será apresentada ao professor)

```bash
java -cp bin execucao.SistemaEstoque
```

### Versão GUI (extra — interface gráfica)

```bash
java -cp bin interface_grafica.AppGrafico
```

---

## 📚 Conceitos de POO Aplicados por Arquivo

### `modelo/Produto.java`
| Conceito | Onde |
|---|---|
| **Classe modelo** | Classe `Produto` com responsabilidade única de representar um produto |
| **Atributos privados** | `private int codigo`, `private String sku`, `private double precoCusto`, etc. |
| **Encapsulamento** | Todos os atributos `private`, acesso via `getters` e `setters` públicos |
| **Construtor padrão** | `public Produto()` — inicializa tudo com valores "vazios" |
| **Construtor parametrizado** | `public Produto(String sku, String nome, ...)` — cria produto completo |
| **Métodos de negócio** | `calcularMargemLucro()`, `calcularValorTotalEstoque()`, `darEntradaEstoque()`, `darSaidaEstoque()`, `ativarAnuncio()`, `pausarAnuncio()`, `exibirInformacoes()` |
| **Atributo estático** | `private static int contadorCodigo` — gera códigos sequenciais |
| **Operadores lógicos e aritméticos** | Cálculo de margem, validação de quantidade |
| **Estrutura condicional** | `if/else` para validações (divisão por zero, estoque insuficiente) |

### `modelo/Categoria.java`
| Conceito | Onde |
|---|---|
| **Classe modelo** | Classe `Categoria` representando uma categoria de marketplace |
| **Encapsulamento** | Atributos `private` com getters e setters |
| **Construtor padrão e parametrizado** | Dois construtores |
| **Método de negócio** | `calcularComissao(double valorVenda)` |
| **Atributo estático** | `private static int contadorId` |

### `execucao/SistemaEstoque.java`
| Conceito | Onde |
|---|---|
| **Classe de execução** | Contém `main`, separada das classes modelo |
| **Scanner** | Entrada de dados do usuário |
| **Array fixo** | `Produto[] produtos = new Produto[50]` |
| **Criação de objetos** | Instanciação de `Produto` e `Categoria` |
| **Manipulação de múltiplos objetos** | 3 produtos pré-cadastrados + cadastro dinâmico |
| **do-while** | Loop principal do menu |
| **switch** | Seleção de opções do menu |
| **for** | Iteração do array de produtos |
| **if/else** | Validações diversas (SKU vazio, duplicado, preço negativo, etc.) |
| **Operadores aritméticos** | Cálculo de valor de venda, comissão, receita líquida |
| **Operadores lógicos** | `&&`, `||`, `!` em validações compostas |
| **try/catch** | Tratamento de `NumberFormatException` na leitura de dados |
| **Chamada de métodos** | Usa `calcularMargemLucro()`, `darEntradaEstoque()`, `darSaidaEstoque()`, etc. |

---

## 🎮 Funcionalidades

| # | Funcionalidade | Console | GUI |
|---|---|:---:|:---:|
| 1 | Cadastrar novo produto | ✅ | ✅ |
| 2 | Listar todos os produtos | ✅ | ✅ (tabela) |
| 3 | Dar entrada no estoque | ✅ | ✅ |
| 4 | Dar saída no estoque (venda) | ✅ | ✅ |
| 5 | Ativar/Pausar anúncio | ✅ | ✅ |
| 6 | Buscar produto por SKU | ✅ | — |
| 7 | Relatório geral | ✅ | ✅ (barra de status) |
| 8 | Editar preço de venda | ✅ | — |

---

## 🛡️ Regras de Negócio

- **SKU duplicado**: bloqueado com mensagem de erro
- **Saída > estoque**: bloqueada com mensagem de erro
- **Preço venda < preço custo**: warning com confirmação do usuário
- **Anúncio pausado**: alerta ao tentar dar saída (venda)
- **Estoque baixo (< 5 unidades)**: destaque visual no relatório/tabela

---

## 🎨 Interface Gráfica (Extra)

A versão GUI utiliza **Java Swing** com tema escuro personalizado:

- **Fundo principal:** `#1A1A1A` (preto carvão)
- **Painéis:** `#252525`
- **Acento (botões e destaques):** `#C6FF00` (verde-lima elétrico)
- **Tabela com linhas alternadas** e cores por status
- **Botões com hover** e efeitos visuais
- **Barra de status** com totais em tempo real
