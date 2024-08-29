package ifsp.edu.source.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.source.DAO.DaoCaixaEletronico;
import ifsp.edu.source.DAO.DaoConta;
import ifsp.edu.source.DAO.DaoMovimento;
import ifsp.edu.source.DAO.DaoUsuario;
import ifsp.edu.source.DAO.DataBaseCom;
import ifsp.edu.source.Model.Conta;
import ifsp.edu.source.Model.Usuario;
import ifsp.edu.source.Request.ContaUsuarioRequest;
import ifsp.edu.source.Response.ContaUsuarioResponse;

@RestController
public class LoginController {
	DataBaseCom database = new DataBaseCom();
	DaoUsuario cadUsuario = new DaoUsuario();
	DaoConta cadConta = new DaoConta();
	DaoCaixaEletronico cadCaixa = new DaoCaixaEletronico();
	DaoMovimento cadMovimento = new DaoMovimento();
	

	
	/*@PostMapping("/login")
	public ResponseEntity<?> Logar(@Validated @RequestBody ContaUsuarioRequest request) {
	    Conta conta = cadConta.buscarContaPorNumero(request.getNumeroConta());
	    
	    if (conta != null) {
	        // Verifica se a conta está ativa
	        if (!"ATIVA".equalsIgnoreCase(conta.getSituacao())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conta não está ativa.");
	        }
	        
	        long idConta = conta.getId();
	        Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);

	        if (usuario != null &&
	            usuario.getNome().trim().equalsIgnoreCase(request.getNome().trim()) &&
	            usuario.getSenha().trim().equals(request.getSenha().trim())) {

	            // Cria um objeto para retornar os dados da conta e do usuário juntos
	            ContaUsuarioResponse response = new ContaUsuarioResponse(conta, usuario);
	            return ResponseEntity.ok(response);
	        }
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou conta não encontrados");
	}*/

	
@PostMapping("/login")
public ResponseEntity<?> Logar(@Validated @RequestBody ContaUsuarioRequest request) {
    System.out.println("Número da conta recebido: " + request.getNumeroConta());
    System.out.println("Senha recebida: " + request.getSenha());

    Conta conta = cadConta.buscarContaPorNumero(request.getNumeroConta());
    if (conta != null) {

        System.out.println("Situação da conta: " + conta.getSituacao());

        if (!"ATIVA".equalsIgnoreCase(conta.getSituacao().trim())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conta não está ativa.");
        }
        
        long idConta = conta.getId();
        Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);

        if (usuario != null &&
            request.getSenha() != null && 
            usuario.getSenha().trim().equals(request.getSenha().trim())) {

            return ResponseEntity.ok(idConta);
        }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou conta não encontrados");
}
}
