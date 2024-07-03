import java.sql.*;

public class BDD {

    static Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    private final String url;
    private final String usuario;
    private final String senha;
    private Connection connection;

    public BDD(String url, String usuario, String senha) {
        this.url = url;
        this.usuario = usuario;
        this.senha = senha;
    }

    public void conectar() throws SQLException {
        this.connection = DriverManager.getConnection(url, usuario, senha);
    }

    public void desconectar() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
        }
    }

    public ResultSet getPersonagens() throws SQLException {
        String sql = "SELECT id, nome FROM jean";
        Statement stmt = this.connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public int verificarPersonagem(int idSelecionado) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM jean WHERE id = " + idSelecionado;
        Statement stmt = this.connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        int total = rs.getInt("total");
        rs.close();
        return total;
    }

    public ResultSet getPersonagem(int personagemId) throws SQLException {
        String sql = "SELECT * FROM jean WHERE id = " + personagemId;
        Statement stmt = this.connection.createStatement();
        return stmt.executeQuery(sql);
    }

    public void atualizarPersonagem(int personagemId, int experiencia, int nivel, int forca, int defesa, double vida) throws SQLException {
        String updateSql = "UPDATE jean SET experiencia = " + experiencia +
                ", nivel = " + nivel +
                ", forca = " + forca +
                ", defesa = " + defesa +
                ", vida = " + vida +
                " WHERE id = " + personagemId;
        Statement stmt = this.connection.createStatement();
        stmt.executeUpdate(updateSql);
    }
}
