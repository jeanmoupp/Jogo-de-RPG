# JogoRPG

Trata-se de um projeto de um jogo RPG simples em Java, que utiliza a programação orientada a objetos. O objetivo do jogo é ajustar os atributos de diferentes personagens conforme especificado.

## Descrição do Jogo

- **Jean**: Um personagem inicial com habilidades equilibradas.
- **Aria**: Tem um pouco menos de força, mas compensa com defesa.
- **Thor**: Forte e com mais vida, mas com menos defesa.
- **Luna**: Um personagem balanceado com boas habilidades gerais.
- **Kai**: Forte com uma quantidade razoável de vida e defesa.

## Funcionalidades

- Seleção de personagens.
- Visualização e ajuste dos atributos dos personagens.
- Ações de jogo como atacar, defender e ganhar experiência.
- Conexão com banco de dados MySQL para salvar e carregar os atributos dos personagens.

## Estrutura do Projeto

O projeto é composto pelas seguintes classes:

1. `RPGGame`: Classe principal que inicia o jogo.
2. `Personagem`: Classe base para os personagens com atributos comuns.
3. `BDD`: Classe responsável pela conexão com o banco de dados MySQL e operações de CRUD.

## Pré-requisitos

- Java 8 ou superior
- MySQL
- Conector JDBC para MySQL

## Configuração do Banco de Dados

Execute o seguinte script SQL para criar o banco de dados e a tabela necessária:

```sql
-- Criar o banco de dados
CREATE DATABASE JogoRPG;

-- Usar o banco de dados criado
USE JogoRPG;

-- Criar a tabela personagens
CREATE TABLE jean (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nivel INT,
    experiencia INT,
    forca INT,
    defesa INT,
    vida DOUBLE
);

-- Inserção de personagens
INSERT INTO jean (nome, nivel, experiencia, forca, defesa, vida)
VALUES 
('Jean', 1, 0, 10, 5, 100.0),
('Aria', 1, 0, 8, 7, 90.0),
('Thor', 1, 0, 15, 4, 110.0),
('Luna', 1, 0, 9, 6, 95.0),
('Kai', 1, 0, 12, 5, 105.0);
```

## Configuração do Projeto

Clone o repositório:

```sh
git clone https://github.com/seu-usuario/JogoRPG.git
cd JogoRPG
```

Adicione o conector JDBC para MySQL ao seu projeto.

Atualize as credenciais do banco de dados na classe `BDD`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/JogoRPG";
private static final String USER = "seu_usuario";
private static final String PASSWORD = "sua_senha";
```

Compile e execute o programa:
```sh
java RPGGame.java
java RPGGame
```

## Uso

Ao iniciar o jogo, você verá as seguintes opções:

1. **Créditos**: Exibe informações sobre o jogo.
2. **Selecione o Personagem (Iniciar Jogo)**: Permite escolher um personagem para iniciar o jogo.
3. **Sair**: Encerra o jogo.

### Exemplo de Jogo

```java
┌───────────────────────────────────────────────────────────┐
│                                                           │
│             Bem-vindo ao Jogo de RPG!                     │
│                                                           │
├───────────────────────────────────────────────────────────┤
│                                                           │
│        1. Créditos                                        │
│        2. Selecione o Personagem (Iniciar Jogo)           │
│        3. Sair                                            │
│                                                           │
└───────────────────────────────────────────────────────────┘
Escolha uma opção: 2

┌──────────────────────────────────────────────────────────┐
│               Selecionar Personagem                      │
└──────────────────────────────────────────────────────────┘
1. Jean
2. Aria
3. Thor
4. Luna
5. Kai

Digite o número do personagem: 1

┌──────────────────────────────────────────────────────────┐
│              Você escolheu: Jean                         │
└──────────────────────────────────────────────────────────┘
Escolha uma ação:

[1] Atacar
[2] Defender
[3] Ganhar Experiência
[4] Sair
Escolha: 1

Você ataca e causa 20 de dano!
```

## Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

## Licença

Este projeto está licenciado sob a MIT License.

## Contato

Autor: Jean de Andrade Barbosa
Email: jeanandradepasa@gmail.com

---

## Código

### RPGGame.java

```java
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RPGGame {
    private static Personagem personagemSelecionado;
    private static final BDD bdd = new BDD("jdbc:mysql://localhost:3306/JogoRPG", "root", "senacrs");

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            bdd.conectar();
            exibirMenuPrincipal();
            boolean jogoAtivo = true;

            while (jogoAtivo) {
                System.out.print("Escolha uma opção: ");
                try {
                    int escolha = scanner.nextInt();
                    scanner.nextLine(); // Limpar o buffer

                    switch (escolha) {
                        case 1 -> novoJogo();
                        case 2 -> {
                            selecionarPersonagem(scanner);
                            if (personagemSelecionado != null) {
                                iniciarJogo(scanner);
                            }
                        }
                        case 3 -> {
                            System.out.println("Saindo do jogo...");
                            jogoAtivo = false;
                        }
                        default -> System.out.println("Opção inválida! Tente novamente.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Entrada inválida! Por favor, insira um número.");
                    scanner.nextLine(); // Limpar o buffer
                }
            }
            bdd.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("┌───────────────────────────────────────────────────────────┐");
        System.out.println("│                                                           │");
        System.out.println("│             Bem-vindo ao Jogo de RPG!                     │");
        System.out.println("│                                                           │");
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("│                                                           │");
        System.out.println("│        1. Créditos                                        │");
        System.out.println("│        2. Selecione o Personagem (Iniciar Jogo)           │");
        System.out.println("│        3. Sair                                            │");
        System.out.println("│                                                           │");
        System.out.println("└───────────────────────────────────────────────────────────┘");
    }

    private static void novoJogo() {
        System.out.println("┌──────────────────────────────────────────────────────────┐");
        System.out.println("│                    BY: JΛP.7                             │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
    }

    private static void selecionarPersonagem(Scanner scanner) {
        try {
            ResultSet rs = bdd.getPersonagens();

            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│               Selecionar Personagem                      │");
            System.out.println("└──────────────────────────────────────────────────────────┘");
            System.out.println("Selecione um personagem:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                System.out.println(id + ". " + nome);
            }
            rs.close();

            System.out.print("Digite o número do personagem: ");
            try {
                int idSelecionado = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer

                if (bdd.verificarPersonagem(idSelecionado) == 0) {
                    System.out.println("Personagem inválido! Tente novamente.");
                    personagemSelecionado = null;
                } else {
                    rs = bdd.getPersonagem(idSelecionado);
                    if (rs.next()) {
                        personagemSelecionado = new Personagem(
                                rs.getInt("id"),
                                rs.getString("nome"),
                                rs.getInt("nivel"),
                                rs.getInt("experiencia"),
                                rs.getInt("forca"),
                                rs.getInt("defesa"),
                                rs.getDouble("vida")
                        );
                        System.out.println("┌──────────────────────────────────────────────────────────┐");
                        System.out.println("│        Personagem selecionado com sucesso!               │");
                        System.out.println("└──────────────────────────────────────────────────────────┘");
                    }
                    rs.close();
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine(); // Limpar o buffer
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void iniciarJogo(Scanner scanner) {
        if (personagemSelecionado == null) {
            System.out.println("Nenhum personagem selecionado.");
            return;
        }

        boolean continuar = true;
        while (continuar) {
            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│              Você escolheu: " + personagemSelecion

ado.getNome() + "                     │");
            System.out.println("└──────────────────────────────────────────────────────────┘");
            System.out.println("Escolha uma ação:");
            System.out.println("[1] Atacar");
            System.out.println("[2] Defender");
            System.out.println("[3] Ganhar Experiência");
            System.out.println("[4] Sair");

            System.out.print("Escolha: ");
            try {
                int acao = scanner.nextInt();
                scanner.nextLine(); // Limpar o buffer

                switch (acao) {
                    case 1 -> atacar();
                    case 2 -> defender();
                    case 3 -> ganharExperiencia();
                    case 4 -> {
                        System.out.println("Saindo do jogo...");
                        continuar = false;
                    }
                    default -> System.out.println("Ação inválida! Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine(); // Limpar o buffer
            }
        }
    }

    private static void atacar() {
        System.out.println("Você ataca e causa 20 de dano!");
        // Atualize os atributos do personagem no banco de dados aqui, se necessário
    }

    private static void defender() {
        System.out.println("Você se defende e reduz o dano recebido em 10!");
        // Atualize os atributos do personagem no banco de dados aqui, se necessário
    }

    private static void ganharExperiencia() {
        personagemSelecionado.setExperiencia(personagemSelecionado.getExperiencia() + 10);
        System.out.println("Você ganhou 10 de experiência!");
        try {
            bdd.atualizarExperiencia(personagemSelecionado.getId(), personagemSelecionado.getExperiencia());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

### Personagem.java

```java
public class Personagem {
    private int id;
    private String nome;
    private int nivel;
    private int experiencia;
    private int forca;
    private int defesa;
    private double vida;

    public Personagem(int id, String nome, int nivel, int experiencia, int forca, int defesa, double vida) {
        this.id = id;
        this.nome = nome;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.forca = forca;
        this.defesa = defesa;
        this.vida = vida;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public int getForca() {
        return forca;
    }

    public void setForca(int forca) {
        this.forca = forca;
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public double getVida() {
        return vida;
    }

    public void setVida(double vida) {
        this.vida = vida;
    }
}
```

### BDD.java

```java
import java.sql.*;

public class BDD {
    private final String url;
    private final String user;
    private final String password;
    private Connection conexao;

    public BDD(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void conectar() throws SQLException {
        conexao = DriverManager.getConnection(url, user, password);
    }

    public void desconectar() throws SQLException {
        if (conexao != null && !conexao.isClosed()) {
            conexao.close();
        }
    }

    public ResultSet getPersonagens() throws SQLException {
        String query = "SELECT * FROM jean";
        Statement stmt = conexao.createStatement();
        return stmt.executeQuery(query);
    }

    public int verificarPersonagem(int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM jean WHERE id = ?";
        PreparedStatement stmt = conexao.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        return count;
    }

    public ResultSet getPersonagem(int id) throws SQLException {
        String query = "SELECT * FROM jean WHERE id = ?";
        PreparedStatement stmt = conexao.prepareStatement(query);
        stmt.setInt(1, id);
        return stmt.executeQuery();
    }

    public void atualizarExperiencia(int id, int experiencia) throws SQLException {
        String query = "UPDATE jean SET experiencia = ? WHERE id = ?";
        PreparedStatement stmt = conexao.prepareStatement(query);
        stmt.setInt(1, experiencia);
        stmt.setInt(2, id);
        stmt.executeUpdate();
        stmt.close();
    }
}
```

### Estrutura do Projeto

```
JogoRPG/
│
├── RPGGame.java
├── Personagem.java
└── BDD.java
```

## Melhorias Futuras

- Implementar sistema de combate mais detalhado.
- Adicionar mais personagens e habilidades.
- Implementar uma interface gráfica.

## Contato

Autor: Jean de Andrade Barbosa
Email: jeanandradepasa@gmail.com
