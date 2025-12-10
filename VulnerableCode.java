import java.io.*;
import java.sql.*;
import java.security.MessageDigest;

public class VulnerableDemo {

    // ======================
    // HARD-CODED CREDENTIALS
    // ======================
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "123456";  // SECRET HARDCODED

    // API Key fake em formato real → secret scanners detectam
    private static final String API_KEY = "sk_live_1234567890abcdefghijklmnopqrstuvwxyz";

    // ======================
    // SQL INJECTION
    // ======================
    public boolean login(String user, String pass) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/app", DB_USER, DB_PASSWORD);

            // SQL INJECTION DETECTÁVEL
            String sql = "SELECT * FROM users WHERE username = '" + user + "' AND password = '" + pass + "'";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================
    // HASH MD5 INSEGURO
    // ======================
    public String hashSenha(String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5"); // INSEGURO
            byte[] hash = md.digest(senha.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======================
    // XSS CLÁSSICO
    // ======================
    public String pagina(String nome) {
        return "<html><body><h1>Olá " + nome + "</h1></body></html>"; // sem sanitização
    }

    // ======================
    // DESERIALIZAÇÃO INSEGURA
    // ======================
    public Object desserializar(byte[] dados) {
        try {
            // Código padrão que ferramentas detectam como "insecure deserialization"
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(dados));
            return ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ======================
    // RCE DETECTÁVEL
    // ======================
    public void executar(String cmd) {
        try {
            Runtime.getRuntime().exec(cmd); // muitas ferramentas marcam como crítico
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
