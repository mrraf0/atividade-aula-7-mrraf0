import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ATENÇÃO
 * ==========================================================
 * ESTE CÓDIGO É INTENCIONALMENTE VULNERÁVEL PARA TESTES.
 *
 * Ele foi criado *exclusivamente* para acionar ferramentas
 * de análise estática (SAST) e dinâmica (DAST), como:
 * CodeQL, Codacy, SonarQube, etc.
 *
 * NÃO USE EM PRODUÇÃO.
 * ==========================================================
 */
public class VulnerableCode {

    // ==========================================================
    // 1) CREDENCIAIS EM CÓDIGO (HARDCODED CREDENTIALS)
    // ==========================================================
    // Ferramentas de secret scanning devem identificar:
    // - usuário exposto
    // - senha exposta
    // - API key exposta
    private static final String DB_URL = "jdbc:mysql://localhost:3306/minha_aplicacao";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "senha_super_secreta";
    private static final String API_KEY = "sk_live_999999999999_api_key_insegura";

    /**
     * LOGIN EXTREMAMENTE INSEGURO
     *
     * Vulnerabilidades demonstradas:
     * - SQL Injection (concatenação direta)
     * - Uso de Statement ao invés de PreparedStatement
     * - Tratamento genérico de exceções
     * - Print de stack trace (vazamento de detalhes)
     */
    public boolean loginInseguro(String username, String password) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // ==========================================================
            // 2) SQL INJECTION CLÁSSICO
            // ==========================================================
            String sql = "SELECT * FROM usuarios WHERE username = '" + username +
                         "' AND password = '" + password + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            return rs.next();

        } catch (Exception e) {
            // ==========================================================
            // 3) TRATAMENTO DE EXCEÇÃO INSEGURO
            // ==========================================================
            System.out.println("Erro durante login:");
            e.printStackTrace(); // inseguro
            return false;
        }
    }

    /**
     * BUSCA DE USUÁRIO INSEGURA
     *
     * Vulnerabilidade:
     * - SQL Injection via LIKE '%input%'
     */
    public void buscarUsuario(String searchTerm) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // ==========================================================
            // 4) SQL INJECTION (LIKE '%" + input + "%')
            // ==========================================================
            String sql = "SELECT * FROM usuarios WHERE nome LIKE '%" + searchTerm + "%'";
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Usuário encontrado: " + rs.getString("nome"));
            }

        } catch (Exception e) {
            e.printStackTrace(); // inseguro
        }
    }

    /**
     * HASH DE SENHA INSEGURO
     *
     * Vulnerabilidade:
     * - Uso de MD5 sem salt
     */
    public String gerarHashInseguro(String senha) {
        try {
            // ==========================================================
            // 5) USO DE ALGORITMO CRIPTOGRÁFICO FRACO (MD5)
            // ==========================================================
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(senha.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            String hash = sb.toString();
            System.out.println("Hash MD5 inseguro: " + hash);
            return hash;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * GERAÇÃO DE HTML INSEGURO — XSS
     *
     * Vulnerabilidade:
     * - Saída não sanitizada gerando XSS
     */
    public String gerarHtmlInseguro(String nome) {

        // ==========================================================
        // 6) XSS (injeção direta no HTML)
        // ==========================================================
        return "<html>" +
               "<body>" +
               "<h1>Olá, " + nome + "!</h1>" +
               "<p>Painel de controle.</p>" +
               "</body>" +
               "</html>";
    }

    /**
     * MÉTODO QUE GERA ERRO INTENCIONAL
     *
     * Vulnerabilidade:
     * - Exceção genérica
     * - Vazamento de stack trace
     */
    public void operacaoCritica() {
        try {
            String t = null;
            t.length(); // causa NullPointerException intencionalmente
        } catch (Exception e) {
            System.out.println("Erro crítico:");
            e.printStackTrace(); // inseguro
        }
    }

    /**
     * Método main para simular execuções inseguras
     */
    public static void main(String[] args) {

        VulnerableCode app = new VulnerableCode();

        System.out.println("== Teste de login inseguro ==");
        app.loginInseguro("admin", "123' OR '1'='1");

        System.out.println("\n== Teste de busca insegura ==");
        app.buscarUsuario("teste%' OR '1'='1");

        System.out.println("\n== Teste de hash inseguro (MD5) ==");
        app.gerarHashInseguro("senhaFraca");

        System.out.println("\n== Teste de XSS ==");
        System.out.println(app.gerarHtmlInseguro("<script>alert('XSS');</script>"));

        System.out.println("\n== Teste de exceções inseguras ==");
        app.operacaoCritica();
    }
}
