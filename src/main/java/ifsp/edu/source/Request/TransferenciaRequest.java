package ifsp.edu.source.Request;

public class TransferenciaRequest {
	    private long idContaRemetente; // og long
	    private String numeroContaDestinatario;
	    private double valorTransferencia;
	    private String senha;

	    // Getters e Setters
	    public long getIdContaRemetente() {// og long
	        return idContaRemetente;
	    }

	    public void setIdContaRemetente(long idContaRemetente) {// og long
	        this.idContaRemetente = idContaRemetente;
	    }

	    public String getNumeroContaDestinatario() {
	        return numeroContaDestinatario;
	    }

	    public void setNumeroContaDestinatario(String numeroContaDestinatario) {
	        this.numeroContaDestinatario = numeroContaDestinatario;
	    }

	    public double getValorTransferencia() {
	        return valorTransferencia;
	    }

	    public void setValorTransferencia(double valorTransferencia) {
	        this.valorTransferencia = valorTransferencia;
	    }
	    
		public String getSenha() {
			return senha;
		}
		public void setSenha(String senha) {
			this.senha = senha;
		}
}
