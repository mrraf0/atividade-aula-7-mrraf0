import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;

/**
 * ==========================================================
 *  CÓDIGO INTENCIONALMENTE VULNERÁVEL — SOMENTE PARA TESTES
 * ==========================================================
 *
 * Contém:
 *  - Credenciais Hardcoded
 *  - SQL Injection
 *  - MD5 inseguro
 *  - Possível XSS
 *  - Exposição de stacktrace
 *  - Comentários contendo dados sensíveis (má prática)
 *
 * NÃO USE EM PRODUÇÃO. APENAS PARA TREINOS/SAST.
 */
public class VulnerableExample {

    // ============================
    // CREDENCIAIS HARDCODED
    // ============================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/app_teste";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "senha_muito_insegura_123";

    // Token fictício exposto no código (má prática)
    private static final String API_KEY = "sk_live_9999999_token_exposto_inseguro";

    /**
     * SQL Injection clássico
     * - Concatena valores diretamente
     * - Não usa PreparedStatement
     */
    public boolean loginInseguro(String usuario, String senha) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL Injection proposital
            String sql = "SELECT * FROM usuarios WHERE username = '" + usuario +
                         "' AND password = '" + senha + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return rs.next();

        } catch (Exception e) {
            System.out.println("Erro no login:");
            e.printStackTrace(); // inseguro
            return false;
        }
    }

    /**
     * SQL Injection usando LIKE
     */
    public void buscarUsuario(String termo) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            String sql = "SELECT * FROM usuarios WHERE nome LIKE '%" + termo + "%'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Encontrado: " + rs.getString("nome"));
            }

        } catch (Exception e) {
            e.printStackTrace(); // inseguro
        }
    }

    /**
     * Uso de MD5 sem salt — extremamente inseguro
     */
    public String gerarHashMD5(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(senha.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));

            String hash = sb.toString();
            System.out.println("MD5 inseguro gerado: " + hash);
            return hash;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Possível XSS
     * - Entrada do usuário vai direto para HTML
     */
    public String gerarHtmlInseguro(String nome) {
        return "<html>" +
               "<body>" +
               "<h1>Bem-vindo, " + nome + "!</h1>" + // vulnerável
               "</body>" +
               "</html>";
    }

    /**
     * Tratamento inseguro de exceções com vazamento de detalhes
     */
    public void operacaoCritica() {
        try {
            String x = null;
            x.length(); // NPE proposital
        } catch (Exception e) {
            System.out.println("Erro crítico ocorrido:");
            e.printStackTrace(); // vazamento de detalhes sensíveis
        }
    }

    public static void main(String[] args) {
        VulnerableExample app = new VulnerableExample();

        System.out.println("\n--- Testando Login Inseguro (SQL Injection) ---");
        app.loginInseguro("admin", "123' OR '1'='1");

        System.out.println("\n--- Testando Busca Insegura ---");
        app.buscarUsuario("teste%' OR '1'='1");

        System.out.println("\n--- Testando MD5 inseguro ---");
        app.gerarHashMD5("senha123");

        System.out.println("\n--- Testando XSS ---");
        System.out.println(app.gerarHtmlInseguro("<script>alert('XSS');</script>"));

        System.out.println("\n--- Testando Operação Crítica ---");
        app.operacaoCritica();
    }
}
