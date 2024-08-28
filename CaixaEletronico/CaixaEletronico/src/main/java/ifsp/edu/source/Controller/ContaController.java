package ifsp.edu.source.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.source.DAO.DaoCaixaEletronico;
import ifsp.edu.source.DAO.DaoConta;
import ifsp.edu.source.DAO.DaoMovimento;
import ifsp.edu.source.DAO.DaoUsuario;
import ifsp.edu.source.DAO.DataBaseCom;
import ifsp.edu.source.Model.Conta;
import ifsp.edu.source.Model.Movimento;
import ifsp.edu.source.Model.Usuario;


@RestController
public class ContaController {
	DataBaseCom database = new DataBaseCom();
	DaoUsuario cadUsuario = new DaoUsuario();
	DaoConta cadConta = new DaoConta();
	DaoCaixaEletronico cadCaixa = new DaoCaixaEletronico();
	DaoMovimento cadMovimento = new DaoMovimento();
	
	@PostMapping("/saldo")
	public ResponseEntity<?> consultarSaldo(@RequestBody Map<String, Long> request) {
	    Long idConta = request.get("idConta");
	    Conta conta = cadConta.buscarContaPorId(idConta);

	    if (conta != null) {
	        double saldo = conta.getValor();
	        return ResponseEntity.ok(saldo);
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
	}
	
	/*@PostMapping("/extrato")
	public ResponseEntity<List<Movimento>> obterExtrato(@RequestBody Map<String, Long> request) {
	    Long idConta = request.get("idConta");
	    //List<Movimento> extrato = cadMovimento.obterExtrato(idConta);
	    List<Movimento> extrato = cadMovimento.obterExtratoComNomes(idConta);

	    if (extrato.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	    return ResponseEntity.ok(extrato);
	}*/
	
	@PostMapping("/extrato")
	public ResponseEntity<Map<String, Object>> obterExtrato(@RequestBody Map<String, String> body) {	    long idConta = Long.parseLong(body.get("numeroConta"));
	    List<Movimento> extrato = cadMovimento.obterExtratoComNomes(idConta);

	    Map<String, Object> response = new HashMap<>();
	    if (extrato != null && !extrato.isEmpty()) {
	        response.put("extrato", extrato); // Alinhar com o nome da chave
	        return ResponseEntity.ok(response);
	    } else {
	        response.put("message", "Nenhum movimento encontrado ou conta não encontrada.");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	}
	
	@GetMapping("/conta")
	public ResponseEntity<Object> verificarConta(@RequestParam String nome, @RequestParam String numeroConta, @RequestParam String senha) {
	    // Lógica para verificar a conta
	    Conta conta = cadConta.buscarContaPorNumero(numeroConta);
	    if (conta != null) {
	        long idConta = conta.getId();
	        Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);
	        if (usuario != null && usuario.getNome().equals(nome) && usuario.getSenha().equals(senha)) {
	            return ResponseEntity.ok(usuario);
	        }
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou conta não encontrados");
	}
	
	@GetMapping("/saldo")
    public ResponseEntity<Map<String, Object>> consultarSaldo(@RequestParam long idConta) {
        Conta conta = cadConta.buscarContaPorId(idConta);
        Map<String, Object> response = new HashMap<>();
        if (conta != null) {
            response.put("saldo", conta.getValor());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Conta não encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
