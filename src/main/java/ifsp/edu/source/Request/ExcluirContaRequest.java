package ifsp.edu.source.Request;

public class ExcluirContaRequest {
    private String numeroConta;
    private String senha; // Adicionado para armazenar a senha

    // Getters e Setters
    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
