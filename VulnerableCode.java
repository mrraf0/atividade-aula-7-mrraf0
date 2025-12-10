import java.io.*;
import java.security.MessageDigest;
import java.sql.*;

public class UserService {

    // ==========================================================
    // 1 — CREDENCIAIS HARDCODED (com aparência de credenciais reais)
    // ==========================================================
    private static final String DB_URL = "jdbc:mysql://192.168.15.20:3306/app_users";
    private static final String DB_USER = "app_user";
    private static final String DB_PASSWORD = "P@ssw0rd2025!"; // senha plausível

    // KEY com formato idêntico ao de APIs reais (Stripe-like)
    private static final String PAYMENT_PRIVATE_KEY =
            "sk_live_51Nsx8GJbbU9C0yWgFzKe83pL9fbYJxwu7YgH2xbYqV8KPpXlC9Qv8F4C00c7r8y89V";

    // ==========================================================
    // 2 — SQL INJECTION ENCAPSULADO EM UM MÉTODO REALISTA
    // ==========================================================
    public boolean authenticateUser(String email, String password) {

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Isto parece real, e ferramentas detectam a concatenação:
            String query =
                    "SELECT id, email FROM users WHERE email = '" + email +
                    "' AND password_hash = '" + password + "'";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            return rs.next();

        } catch (Exception e) {
            // printStackTrace → informação sensível em log
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================================
    // 3 — HASH MD5, MAS DE FORMA "DISFARÇADA" COMO PSEUDO-CRYPTO PRODUCTION CODE
    // ==========================================================
    public String computePasswordHash(String plainPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // inseguro, detectável
            md.update(plainPassword.getBytes());
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==========================================================
    // 4 — XSS SUTIL: HTML DE TEMPLATE + VALOR NÃO SANITIZADO
    // ==========================================================
    public String renderUserProfile(String fullName) {

        // Parece um template real de dashboard
        return """
               <div class="profile-container">
                   <h2>Perfil do Usuário</h2>
                   <p><strong>Nome completo:</strong> %s</p>
                   <p>Status da conta: Ativa</p>
               </div>
               """.formatted(fullName); // sem sanitização → XSS detectável
    }

    // ==========================================================
    // 5 — DESERIALIZAÇÃO VULNERÁVEL (com contexto realista)
    // ==========================================================
    public Object loadSessionData(byte[] binaryData) {
        try {
            // prática real em sistemas legados: deserializar sessão no servidor
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(binaryData));
            return ois.readObject(); // inseguro — permite gadget chains

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ==========================================================
    // 6 — RCE “DISFARÇADO” EM PROCESSO DE UTILIDADE
    // ==========================================================
    public void runSystemCheck(String cmd) {
        try {
            // parece algo legítimo, como rodar um script de healthcheck
            Runtime.getRuntime().exec(cmd); // detectável como potencial RCE
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ==========================================================
    // MÉTODO PRINCIPAL PARA TESTES
    // ==========================================================
    public static void main(String[] args) {
        UserService u = new UserService();

        System.out.println("Autenticando...");
        u.authenticateUser("admin@example.com", "admin123");

        System.out.println("Hash inseguro:");
        System.out.println(u.computePasswordHash("senhaFraca123"));

        System.out.println("Render HTML:");
        System.out.println(u.renderUserProfile("<script>alert('XSS');</script>"));
    }
}
