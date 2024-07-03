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

    public int getExperiencia() {
        return experiencia;
    }

    public int getForca() {
        return forca;
    }

    public int getDefesa() {
        return defesa;
    }

    public double getVida() {
        return vida;
    }

    public void atacar() {
        int dano = forca * 2;
        System.out.println("Você ataca e causa " + dano + " de dano!");
        experiencia += 10;
    }

    public void defender() {
        double danoRecebido = (defesa / 2.0) + 5;
        vida -= danoRecebido;
        System.out.println("Você se defende e recebe " + danoRecebido + " de dano!");
        if (vida <= 0) {
            morrer();
        }
    }

    public void ganharExperiencia(int xp) {
        experiencia += xp;
        if (experiencia >= 100) {
            nivel++;
            experiencia -= 100;
            forca += 5;
            defesa += 3;
            vida += 20;
            System.out.println("┌──────────────────────────────────────────────────────────┐");
            System.out.println("│           Você subiu para o nível " + nivel + "!              │");
            System.out.println("└──────────────────────────────────────────────────────────┘");
        }
    }

    public void exibirStatus() {
        System.out.println("Nível: " + nivel);
        System.out.println("Experiência: " + experiencia);
        System.out.println("Força: " + forca);
        System.out.println("Defesa: " + defesa);
        System.out.println("Vida: " + vida);
    }

    public void morrer() {
        vida = 0;
        System.out.println("┌──────────────────────────────────────────────────────────┐");
        System.out.println("│                " + nome + " morreu!                       │");
        System.out.println("└──────────────────────────────────────────────────────────┘");
    }
}
