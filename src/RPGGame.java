import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class RPGGame {
    private static Personagem personagemSelecionado;  // Personagem selecionado
    private static final BDD bdd = new BDD("jdbc:mysql://localhost:3306/jean", "root", "senacrs");

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
                        case 1 -> novoJogo(scanner);
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
        System.out.println("│                                                    │");
        System.out.println("│             Bem-vindo ao Jogo de RPG!              │");
        System.out.println("│                                                    │");
        System.out.println("├───────────────────────────────────────────────────────────┤");
        System.out.println("│                                                    │");
        System.out.println("│        1. Créditos                                 │");
        System.out.println("│        2. Selecione o Personagem (Iniciar Jogo)    │");
        System.out.println("│        3. Sair                                     │");
        System.out.println("│                                                    │");
        System.out.println("└───────────────────────────────────────────────────────────┘");
    }

    private static void novoJogo(Scanner scanner) {
        System.out.println("┌──────────────────────────────────────────────────────────┐");
        System.out.println("│                    BY: JΛP.7                      │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
        if (personagemSelecionado == null) {
        } else {
            iniciarJogo(scanner);
        }
    }

    private static void selecionarPersonagem(Scanner scanner) {
        try {
            ResultSet rs = bdd.getPersonagens();

            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│               Selecionar Personagem                │");
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
                        System.out.println("│        Personagem selecionado com sucesso!         │");
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
/**
 * as
 * ss
 * sa
 * @param scanner po
 */
    public static void iniciarJogo(Scanner scanner) {
        if (personagemSelecionado == null) {
            System.out.println("Nenhum personagem selecionado.");
            return;
        }

        boolean continuar = true;
        while (continuar) {
            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│              Você escolheu: " + personagemSelecionado.getNome() + "                   │");
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
                    case 1 -> personagemSelecionado.atacar();
                    case 2 -> personagemSelecionado.defender();
                    case 3 -> {
                        personagemSelecionado.ganharExperiencia(30);
                        System.out.println("Você ganha 30 de experiência!");
                    }
                    case 4 -> continuar = false;
                    default -> System.out.println("Ação inválida!");
                }

                if (personagemSelecionado.getVida() <= 0) {
                    continuar = false;
                } else {
                    try {
                        bdd.atualizarPersonagem(
                                personagemSelecionado.getId(),
                                personagemSelecionado.getExperiencia(),
                                personagemSelecionado.getNivel(),
                                personagemSelecionado.getForca(),
                                personagemSelecionado.getDefesa(),
                                personagemSelecionado.getVida()
                        );
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    System.out.println("┌──────────────────────────────────────────────────────────┐");
                    System.out.println("│                    Status Atual                    │");
                    System.out.println("└──────────────────────────────────────────────────────────┘");
                    personagemSelecionado.exibirStatus();
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                scanner.nextLine(); // Limpar o buffer
            }
        }

        if (personagemSelecionado.getVida() <= 0) {
            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│            Fim do Jogo - Personagem Morto!         │");
            System.out.println("└──────────────────────────────────────────────────────────┘");
        }
    }
}
