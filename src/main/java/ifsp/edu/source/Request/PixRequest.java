public class PixRequest {
    private String idContaRemetente;
    private String numeroContaDestinatario;
    private double valorTransferencia;
    private String senha;

    // Getters e Setters
	    public String getIdContaRemetente() {
	        return idContaRemetente;
	    }

	    public void setIdContaRemetente(String idContaRemetente) {
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
