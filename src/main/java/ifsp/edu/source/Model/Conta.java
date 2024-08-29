package ifsp.edu.source.Model;

public class Conta {
    private long id;
    private tipoConta tipoConta;
    private statusConta statusConta;
    private String valor;
    private String numeroConta;
    private String situacao;

    // Construtor
    public Conta() {
        this.situacao = "ATIVA"; // Define a situação como ATIVA por padrão
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getValor() {
        return numeroConta;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    
    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public tipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(tipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public statusConta getStatusConta() {
        return statusConta;
    }

    public void setStatusConta(statusConta statusConta) {
        this.statusConta = statusConta;
    }

    // Enums for account type and status
    public enum tipoConta {
        CORRENTE,
        POUPANCA
    }

    public enum statusConta {
        OURO,
        PRATA,
        BRONZE
    }
}
