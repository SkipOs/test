package ifsp.edu.source.Controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
import ifsp.edu.source.Request.UsuarioContaRequest;

@RestController
public class CadastroController {
	DataBaseCom database = new DataBaseCom();
	DaoUsuario cadUsuario = new DaoUsuario();
	DaoConta cadConta = new DaoConta();
	DaoCaixaEletronico cadCaixa = new DaoCaixaEletronico();
	DaoMovimento cadMovimento = new DaoMovimento();
	

	
	@PostMapping("/cadastrar")
	public ResponseEntity<Map<String, Object>> Post(@Validated @RequestBody UsuarioContaRequest request) {
	    Conta conta = request.getConta();
	    Usuario usuario = request.getUsuario();

	    // Gerar o número da conta
	    String numeroConta = gerarNumeroContaUnico();
	    conta.setNumeroConta(numeroConta);

	    // Definir o status da conta com base no salário informado
	    double salario = usuario.getSalario();
	    if (salario < 2000) {
	        conta.setStatusConta(Conta.statusConta.BRONZE);
	    } else if (salario >= 2000 && salario < 5000) {
	        conta.setStatusConta(Conta.statusConta.PRATA);
	    } else {
	        conta.setStatusConta(Conta.statusConta.OURO);
	    }

	    long idConta = cadConta.incluir(conta);

	    if (idConta > 0) {  // Verifica se a conta foi inserida corretamente
	        // Associa o ID da conta ao usuário
	        usuario.setIdConta(idConta);
	        cadUsuario.incluir(usuario);

	        // Preparar o retorno como um mapa de resposta JSON
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "Pessoa e conta cadastradas com sucesso!");
	        response.put("numeroConta", numeroConta);
	        response.put("idConta", idConta);

	        return ResponseEntity.ok(response);
	    } else {
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "Erro ao cadastrar conta!");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
	

	// Função para gerar um número de conta único
	private String gerarNumeroContaUnico() {
	    Random random = new Random();
	    String numeroConta;

	    do {
	        numeroConta = String.format("%06d", random.nextInt(1000000));
	    } while (cadConta.verificarNumeroContaExistente(numeroConta));  // Verifica se o número já existe no banco

	    return numeroConta;
	}
	
	/*
	@PostMapping("/cadastrar") // incluir
	public String Post(@Validated @RequestBody Usuario usuario, Conta conta) {
		long idConta = cadConta.incluir(conta);
		
		cadUsuario.incluir(usuario);
		return "Usuario Cadastrada";
		
		if (idConta > 0) {  // Verifica se a conta foi inserida corretamente
            // Associa o ID da conta ao usuário
            usuario.setIdConta(idConta);
            cadUsuario.incluir(usuario);
            return "Usuario e conta cadastradas com sucesso!";
        } else {
            return "Erro ao cadastrar conta!";
        }
	}
	*/
	
	
	/*
	@PostMapping("/cadastrar")
	public String Post(@Validated @RequestBody UsuarioContaRequest request) {
	    Conta conta = request.getConta();
	    Usuario usuario = request.getUsuario();

	    long idConta = cadConta.incluir(conta);

	    if (idConta > 0) {  // Verifica se a conta foi inserida corretamente
	        // Associa o ID da conta ao usuário
	        usuario.setIdConta(idConta);
	        cadUsuario.incluir(usuario);
	        return "Pessoa e conta cadastradas com sucesso!";
	    } else {
	        return "Erro ao cadastrar conta!";
	    }
	}*/
	
	/*@PostMapping("/cadastrar")
	public String Post(@Validated @RequestBody UsuarioContaRequest request) {
	    Conta conta = request.getConta();
	    Usuario usuario = request.getUsuario();

	    // Gerar o número da conta
	    String numeroConta = gerarNumeroContaUnico();
	    conta.setNumeroConta(numeroConta);

	    // Definir o status da conta com base no salário informado
	    double salario = usuario.getSalario();
	    if (salario < 2000) {
	        conta.setStatusConta(Conta.statusConta.BRONZE);
	    } else if (salario >= 2000 && salario < 5000) {
	        conta.setStatusConta(Conta.statusConta.PRATA);
	    } else {
	        conta.setStatusConta(Conta.statusConta.OURO);
	    }

	    long idConta = cadConta.incluir(conta);

	    if (idConta > 0) {  // Verifica se a conta foi inserida corretamente
	        // Associa o ID da conta ao usuário
	        usuario.setIdConta(idConta);
	        cadUsuario.incluir(usuario);
	        return "Pessoa e conta cadastradas com sucesso! Número da conta: " + numeroConta;
	    } else {
	        return "Erro ao cadastrar conta!";
	    }
	}*/
	
	@PostMapping("/usuario")
	public ResponseEntity<Map<String, Object>> cadastrarUsuarioConta(@Validated @RequestBody UsuarioContaRequest request) {
	    Conta conta = request.getConta();
	    Usuario usuario = request.getUsuario();

	    // Gerar o número da conta
	    String numeroConta = gerarNumeroContaUnico();
	    conta.setNumeroConta(numeroConta);
	    
	    double salario = usuario.getSalario();
	    if (salario < 2000) {
	        conta.setStatusConta(Conta.statusConta.BRONZE);
	    } else if (salario >= 2000 && salario < 5000) {
	        conta.setStatusConta(Conta.statusConta.PRATA);
	    } else {
	        conta.setStatusConta(Conta.statusConta.OURO);
	    }

	    long idConta = cadConta.incluir(conta);

	    if (idConta > 0) {
	        // Associa o ID da conta ao usuário
	        usuario.setIdConta(idConta);
	        cadUsuario.incluir(usuario);

	        // Preparar o retorno como um mapa de resposta JSON
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "Pessoa e conta cadastradas com sucesso!");
	        response.put("numeroConta", numeroConta);
	        response.put("idConta", idConta);

	        return ResponseEntity.ok(response);
	    } else {
	        Map<String, Object> response = new HashMap<>();
	        response.put("message", "Erro ao cadastrar conta!");

	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
}
