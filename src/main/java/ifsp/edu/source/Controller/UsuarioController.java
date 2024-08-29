
package ifsp.edu.source.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.source.DAO.DataBaseCom;
import ifsp.edu.source.Model.CaixaEletronico;
import ifsp.edu.source.Model.Conta;
import ifsp.edu.source.Model.Movimento;
import ifsp.edu.source.Model.Usuario;
import ifsp.edu.source.Model.AbstractFactory.Banco.Banco;
import ifsp.edu.source.Model.AbstractFactory.FactoryConta.FactoryContaBronze;
import ifsp.edu.source.Model.AbstractFactory.FactoryConta.FactoryContaOuro;
import ifsp.edu.source.Model.AbstractFactory.FactoryConta.FactoryContaPrata;
import ifsp.edu.source.Request.AtualizarUsuarioRequest;
import ifsp.edu.source.Request.ContaUsuarioRequest;
import ifsp.edu.source.Request.DepositoRequest;
import ifsp.edu.source.Request.ExcluirContaRequest;
import ifsp.edu.source.Request.SaqueRequest;
import ifsp.edu.source.Request.TransferenciaRequest;
import ifsp.edu.source.Request.UsuarioContaRequest;
import ifsp.edu.source.Response.ContaUsuarioResponse;
import ifsp.edu.source.DAO.DaoCaixaEletronico;
import ifsp.edu.source.DAO.DaoConta;
import ifsp.edu.source.DAO.DaoMovimento;
import ifsp.edu.source.DAO.DaoUsuario;

@RestController
public class UsuarioController {
	DataBaseCom database = new DataBaseCom();
	DaoUsuario cadUsuario = new DaoUsuario();
	DaoConta cadConta = new DaoConta();
	DaoCaixaEletronico cadCaixa = new DaoCaixaEletronico();
	DaoMovimento cadMovimento = new DaoMovimento();
	
	@PostMapping("/detalhes-usuario")
	public ResponseEntity<?> obterDetalhesUsuario(@RequestBody Map<String, String> request) {
	    String numeroConta = request.get("numeroConta");

	    // Buscar a conta pelo número da conta
	    Conta conta = cadConta.buscarContaPorNumero(numeroConta);
	    if (conta != null) {
	        long idConta = conta.getId();

	        // Buscar o usuário associado ao id da conta
	        Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);

	        if (usuario != null) {
	            // Criar uma resposta com os detalhes da conta e do usuário
	            ContaUsuarioResponse response = new ContaUsuarioResponse(conta, usuario);
	            return ResponseEntity.ok(response);
	        }
	    }
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta ou usuário não encontrados");
	}
	
	@GetMapping("/detalhes-usuario")
	public ResponseEntity<Map<String, Object>> obterDetalhesUsuario(@RequestParam long numeroConta) {
	    // Lógica para buscar o usuário pelo número da conta
	    Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(numeroConta);

	    if (usuario != null) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("nome", usuario.getNome());
		response.put("email", usuario.getEmail()); 
		response.put("cpf", usuario.getCpf()); 
		response.put("dataNascimento", usuario.getDataNascimento());
		response.put("cadConta", numeroConta);
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }
	}

	@PostMapping("/atualizar")
	public ResponseEntity<String> atualizarUsuario(@RequestBody AtualizarUsuarioRequest request) {
	    // Buscar conta pelo número
	    Conta conta = cadConta.buscarContaPorNumero(request.getNumeroConta());
	    
	    if (conta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
	    }
	    
	    long idConta = conta.getId();
	    
	    // Buscar usuário associado à conta
	    Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);
	    
	    if (usuario == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
	    }
	    
	    // Atualizar informações do usuário
	    boolean atualizado = false;
	    
	    if (request.getNome() != null && !request.getNome().isEmpty()) {
	        usuario.setNome(request.getNome());
	        atualizado = true;
	    }
	    
	    if (request.getSenha() != null && !request.getSenha().isEmpty()) {
	        usuario.setSenha(request.getSenha());
	        atualizado = true;
	    }
	    
	    if (request.getEmail() != null && !request.getEmail().isEmpty()) {
	        usuario.setEmail(request.getEmail());
	        atualizado = true;
	    }
	    
	    if (request.getCpf() != null && !request.getCpf().isEmpty()) {
	        usuario.setCpf(request.getCpf());
	        atualizado = true;
	    }
	    
	    if (request.getDataNascimento() != null) {
	        //usuario.setDataNascimento(request.getDataNascimento());
	        usuario.setDataNascimento(request.getDataNascimento());
	        atualizado = true;
	    }
	    
	    // Atualizar usuário no banco de dados
	    if (atualizado) {
	        cadUsuario.atualizar(usuario);
	        return ResponseEntity.ok("Usuário atualizado com sucesso");
	    }
	    
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum dado para atualizar");
	}

	@PostMapping("/atualizar-senha")
public ResponseEntity<String> atualizarSenha(@RequestBody Map<String, String> senhaData) {
    String numeroConta = senhaData.get("numeroConta");
    String senhaAtual = senhaData.get("senhaAtual");
    String novaSenha = senhaData.get("novaSenha");

    // Verifique se todos os dados necessários estão presentes
    if (numeroConta == null || senhaAtual == null || novaSenha == null) {
        return ResponseEntity.badRequest().body("Dados incompletos");
    }

	// Buscar a conta pelo número da conta
    Conta conta = cadConta.buscarContaPorNumero(numeroConta);
    if (conta != null) {
        long idConta = conta.getId();

        // Buscar o usuário associado ao id da conta
        Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);

        if (usuario != null && usuario.getSenha().equals(senhaAtual)) {
            // Atualizar a senha do usuário
            usuario.setSenha(novaSenha);
            cadUsuario.atualizar(usuario);
            return ResponseEntity.ok("Senha atualizada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Senha atual incorreta");
        }
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta ou usuário não encontrados");
}

	@PostMapping("/excluir")
	public ResponseEntity<String> excluirConta(@RequestBody ExcluirContaRequest request) {
	    String numeroConta = request.getNumeroConta();
	    
	    // Buscar conta pelo número
	    Conta conta = cadConta.buscarContaPorNumero(numeroConta);
	    
	    if (conta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
	    }
	    
	    // Verificar o saldo da conta
	    double saldo = conta.getValor();
	    
	    if (saldo > 0) {
	        // Retornar o saldo a ser sacado
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo positivo de " + saldo + " encontrado. Por favor, saque o valor antes de encerrar a conta.");
	    }
	    
	    // Atualizar a situação da conta para "INATIVO"
	    conta.setSituacao("INATIVA");
	    cadConta.inativarConta(conta);
	    
	    // Excluir a conta
	    return ResponseEntity.ok("A conta foi inativada com sucesso.");
	}

	




}
