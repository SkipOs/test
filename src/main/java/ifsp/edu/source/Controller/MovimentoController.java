package ifsp.edu.source.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.source.DAO.DaoCaixaEletronico;
import ifsp.edu.source.DAO.DaoConta;
import ifsp.edu.source.DAO.DaoMovimento;
import ifsp.edu.source.DAO.DaoUsuario;
import ifsp.edu.source.DAO.DataBaseCom;
import ifsp.edu.source.Model.CaixaEletronico;
import ifsp.edu.source.Model.Conta;
import ifsp.edu.source.Model.Usuario;
import ifsp.edu.source.Request.DepositoRequest;
import ifsp.edu.source.Request.SaqueRequest;
import ifsp.edu.source.Request.TransferenciaRequest;


@RestController
public class MovimentoController {
	DataBaseCom database = new DataBaseCom();
	DaoUsuario cadUsuario = new DaoUsuario();
	DaoConta cadConta = new DaoConta();
	DaoCaixaEletronico cadCaixa = new DaoCaixaEletronico();
	DaoMovimento cadMovimento = new DaoMovimento();

	@PostMapping("/pix")
public ResponseEntity<String> realizarPix(@RequestBody TransferenciaRequest request) {
    long numeroContaRemetente = request.getNumeroContaRemetente();
    double valorTransferencia = request.getValorTransferencia();
    String numeroContaDestinatario = request.getNumeroContaDestinatario();
    String senha = request.getSenha();

    // Buscar a conta do remetente pelo número
    Conta contaRemetente = cadConta.buscarContaPorNumero(numeroContaRemetente);

    if (contaRemetente == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"Conta remetente não encontrada\"}");
    }

    // Buscar a conta do destinatário pelo número
    Conta contaDestinatario = cadConta.buscarContaPorNumero(numeroContaDestinatario);

    if (contaDestinatario == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"Conta destinatário não encontrada\"}");
    }

    // Verificar se o número da conta remetente é igual ao número da conta destinatário
    if (contaRemetente.getNumeroConta().equals(numeroContaDestinatario)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Não é possível transferir para a mesma conta.\"}");
    }

    // Buscar o usuário associado à conta remetente
    Usuario usuarioRemetente = cadUsuario.buscarUsuarioPorIdConta(contaRemetente.getId());

    if (usuarioRemetente == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"Usuário não encontrado\"}");
    }

    // Verificar a senha do remetente
    if (!usuarioRemetente.getSenha().equals(senha)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"Senha incorreta\"}");
    }

    // Verificar saldo suficiente
    if (contaRemetente.getValor() < valorTransferencia) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Saldo insuficiente na conta remetente\"}");
    }

    // Realizar a transferência PIX
    contaRemetente.setValor(contaRemetente.getValor() - valorTransferencia);
    cadConta.atualizarConta(contaRemetente);

    contaDestinatario.setValor(contaDestinatario.getValor() + valorTransferencia);
    cadConta.atualizarConta(contaDestinatario);

    // Registrar a transferência PIX
    cadMovimento.registrarPix(contaRemetente.getId(), contaDestinatario.getId(), valorTransferencia);

    // Retornar sucesso
    return ResponseEntity.ok("{\"success\": true, \"message\": \"Transferência PIX realizada com sucesso\"}");
}

	
	/*@PostMapping("/transferencia")
	public ResponseEntity<String> transferir(@RequestBody TransferenciaRequest request) {
	    // Buscar conta do remetente pelo ID
	    Conta contaRemetente = cadConta.buscarContaPorId(request.getIdContaRemetente());
	    
	    // Verificar se a conta do remetente existe e se está ativa
	    if (contaRemetente == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta remetente não encontrada");
	    }
	    
	    if (!"ATIVA".equalsIgnoreCase(contaRemetente.getSituacao())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conta remetente não está ativa");
	    }
	    
	    // Verificar se a conta do remetente tem saldo suficiente
	    if (contaRemetente.getValor() < request.getValorTransferencia()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente na conta remetente");
	    }
	    
	    // Buscar conta do destinatário pelo número da conta
	    Conta contaDestinatario = cadConta.buscarContaPorNumero(request.getNumeroContaDestinatario());
	    
	    // Verificar se a conta do destinatário existe e se está ativa
	    if (contaDestinatario == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta destinatário não encontrada");
	    }
	    
	    if (!"ATIVA".equalsIgnoreCase(contaDestinatario.getSituacao())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Conta destinatário não está ativa");
	    }
	    
	    if (contaRemetente.getNumeroConta().equals(contaDestinatario.getNumeroConta())) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível transferir para a mesma conta.");
	    }
	    
	    // Realizar a transferência
	    double valorTransferencia = request.getValorTransferencia();
	    
	    // Subtrair o valor da conta do remetente
	    contaRemetente.setValor(contaRemetente.getValor() - valorTransferencia);
	    cadConta.atualizarConta(contaRemetente);
	    
	    // Adicionar o valor na conta do destinatário
	    contaDestinatario.setValor(contaDestinatario.getValor() + valorTransferencia);
	    cadConta.atualizarConta(contaDestinatario);
	    
	    // Registrar a transferência no histórico de movimentos
	    DaoMovimento daoMovimento = new DaoMovimento();
	    daoMovimento.registrarTransferencia(contaRemetente.getId(), contaDestinatario.getId(), valorTransferencia);
	    
	    return ResponseEntity.ok("Transferência realizada com sucesso");
	}*/
	
	/*@PostMapping("/transferencia")
	public ResponseEntity<String> transferir(@RequestBody TransferenciaRequest request) {
	    long idContaRemetente = request.getIdContaRemetente();
	    double valorTransferencia = request.getValorTransferencia();
	    String numeroContaDestinatario = request.getNumeroContaDestinatario();

	    Conta contaRemetente = cadConta.buscarContaPorId(idContaRemetente);

	    if (contaRemetente == null) {
	        System.out.println("Conta remetente não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta remetente não encontrada");
	    }

	    if (contaRemetente.getValor() < valorTransferencia) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo insuficiente na conta remetente");
	    }

	    Conta contaDestinatario = cadConta.buscarContaPorNumero(numeroContaDestinatario);

	    if (contaDestinatario == null) {
	        System.out.println("Conta destinatário não encontrada");
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta destinatário não encontrada");
	    }

	    contaRemetente.setValor(contaRemetente.getValor() - valorTransferencia);
	    cadConta.atualizarConta(contaRemetente);

	    contaDestinatario.setValor(contaDestinatario.getValor() + valorTransferencia);
	    cadConta.atualizarConta(contaDestinatario);

	    cadMovimento.registrarTransferencia(contaRemetente.getId(), contaDestinatario.getId(), valorTransferencia);

	    return ResponseEntity.ok("{\"success\": true, \"message\": \"Transferência realizada com sucesso\"}");

	}*/
	
	@PostMapping("/transferencia")
	public ResponseEntity<String> transferir(@RequestBody TransferenciaRequest request) {
	    long idContaRemetente = request.getIdContaRemetente();
	    double valorTransferencia = request.getValorTransferencia();
	    String numeroContaDestinatario = request.getNumeroContaDestinatario();
	    String senha = request.getSenha(); // Adicionar campo de senha

	    // Buscar a conta do remetente pelo ID
	    Conta contaRemetente = cadConta.buscarContaPorId(idContaRemetente);

	    if (contaRemetente == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"Conta remetente não encontrada\"}");
	    }

	    // Buscar a conta do destinatário pelo número
	    Conta contaDestinatario = cadConta.buscarContaPorNumero(numeroContaDestinatario);

	    if (contaDestinatario == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"Conta destinatário não encontrada\"}");
	    }

	    // Verificar se o número da conta remetente é igual ao número da conta destinatário
	    if (contaRemetente.getNumeroConta().equals(numeroContaDestinatario)) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Não é possível transferir para a mesma conta.\"}");
	    }

	    // Buscar o usuário associado à conta remetente
	    Usuario usuarioRemetente = cadUsuario.buscarUsuarioPorIdConta(idContaRemetente);

	    if (usuarioRemetente == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"success\": false, \"message\": \"Usuário não encontrado\"}");
	    }

	    // Verificar a senha do remetente
	    if (!usuarioRemetente.getSenha().equals(senha)) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"success\": false, \"message\": \"Senha incorreta\"}");
	    }

	    // Verificar saldo suficiente
	    if (contaRemetente.getValor() < valorTransferencia) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Saldo insuficiente na conta remetente\"}");
	    }

	    // Realizar a transferência
	    contaRemetente.setValor(contaRemetente.getValor() - valorTransferencia);
	    cadConta.atualizarConta(contaRemetente);

	    contaDestinatario.setValor(contaDestinatario.getValor() + valorTransferencia);
	    cadConta.atualizarConta(contaDestinatario);

	    // Registrar a transferência
	    cadMovimento.registrarTransferencia(contaRemetente.getId(), contaDestinatario.getId(), valorTransferencia);

	    // Retornar sucesso
	    return ResponseEntity.ok("{\"success\": true, \"message\": \"Transferência realizada com sucesso\"}");
	}





	
	/*@PostMapping("/deposito")
	public ResponseEntity<String> realizarDeposito(@RequestBody DepositoRequest request) {
	    // Buscar a conta do remetente pelo ID
	    Conta contaRemetente = cadConta.buscarContaPorId(request.getIdContaRemetente());
	    
	    if (contaRemetente != null) {
	        // Adicionar o valor depositado ao saldo da conta do remetente
	        double novoSaldo = contaRemetente.getValor() + request.getValorDeposito();
	        contaRemetente.setValor(novoSaldo);
	        
	        // Atualizar a conta no banco de dados
	        cadConta.atualizarConta(contaRemetente);
	        
	        // Registrar o depósito no histórico de movimentos
	        DaoMovimento daoMovimento = new DaoMovimento();
	        daoMovimento.registrarDeposito(contaRemetente.getId(), request.getValorDeposito());
	        
	        return ResponseEntity.ok("Depósito realizado com sucesso! Novo saldo: " + novoSaldo);
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
	    }
	}*/
	
	@PostMapping("/deposito")
	public ResponseEntity<String> realizarDeposito(@RequestBody DepositoRequest request) {
	    // Buscar a conta do remetente pelo ID
	    Conta contaRemetente = cadConta.buscarContaPorId(request.getIdContaRemetente());

	    if (contaRemetente != null) {
	        // Adicionar o valor depositado ao saldo da conta do remetente
	        double novoSaldo = contaRemetente.getValor() + request.getValorDeposito();
	        contaRemetente.setValor(novoSaldo);

	        // Atualizar a conta no banco de dados
	        cadConta.atualizarConta(contaRemetente);

	        // Registrar o depósito no histórico de movimentos
	        cadMovimento.registrarDeposito(contaRemetente.getId(), request.getValorDeposito());

	        // Retornar a resposta com status 200 e corpo JSON
	        String jsonResponse = String.format(
	            "{\"success\": true, \"mensagem\": \"Depósito realizado com sucesso\", \"novoSaldo\": %.2f}",
	            novoSaldo
	        );
	        return ResponseEntity.ok(jsonResponse);
	    } else {
	        // Retornar resposta com status 404 se a conta não for encontrada
	        String jsonResponse = "{\"success\": false, \"mensagem\": \"Conta não encontrada.\"}";
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
	    }
	}


	
	
	/*@PostMapping("/saque")
	public ResponseEntity<?> realizarSaque(@RequestBody SaqueRequest request) {
	    // Buscar a conta do solicitante pelo ID
	    Conta conta = cadConta.buscarContaPorId(request.getIdConta());
	    CaixaEletronico caixa = cadCaixa.buscarCaixa();
	    
	    if (conta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
	    }

	    double valorSaque = request.getValorSaque();
	    
	    // Verifica se há dinheiro suficiente no caixa
	    if (valorSaque > caixa.getTotalDinheiro()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não há dinheiro suficiente no caixa.");
	    }

	    // Verifica se é possível dispensar o valor solicitado com as notas disponíveis
	    Map<Integer, Integer> notas = caixa.dispensarValor((int) valorSaque);
	    if (notas.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível dispensar o valor solicitado com as notas disponíveis.");
	    }

	    // Atualiza o saldo da conta do solicitante
	    conta.setValor(conta.getValor() - valorSaque);
	    cadConta.atualizarConta(conta);

	    // Atualiza as quantidades de notas no caixa
	    cadCaixa.atualizarCaixa(caixa);

	    // Registrar o saque no histórico de movimentos
	    DaoMovimento daoMovimento = new DaoMovimento();
	    daoMovimento.registrarSaque(conta.getId(), valorSaque);
	    
	    // Retorna a quantidade de notas utilizadas
	    return ResponseEntity.ok(notas);
	}*/
	
	@PostMapping("/saque")
	public ResponseEntity<?> realizarSaque(@RequestBody SaqueRequest request) {
	    // Buscar a conta do solicitante pelo ID
	    Conta conta = cadConta.buscarContaPorId(request.getIdConta());
	    CaixaEletronico caixa = cadCaixa.buscarCaixa();

	    if (conta == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada.");
	    }

	    double valorSaque = request.getValorSaque();
	    
	    // Verifica se é possivel fazer a operação
	    if ((conta.getValor() - valorSaque) < 0) {
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não há dinheiro suficiente na conta.");
	    };
	        
	    
	    // Verifica se há dinheiro suficiente no caixa
	    if (valorSaque > caixa.getTotalDinheiro()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não há dinheiro suficiente no caixa.");
	    }

	    // Verifica se é possível dispensar o valor solicitado com as notas disponíveis
	    Map<Integer, Integer> notas = caixa.dispensarValor((int) valorSaque);
	    if (notas.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não é possível dispensar o valor solicitado com as notas disponíveis.");
	    }

	    // Atualiza o saldo da conta do solicitante
	    conta.setValor(conta.getValor() - valorSaque);
	    cadConta.atualizarConta(conta);

	    // Atualiza as quantidades de notas no caixa
	    cadCaixa.atualizarCaixa(caixa);

	    // Registrar o saque no histórico de movimentos
	    cadMovimento.registrarSaque(conta.getId(), valorSaque);

	    // Retorna a quantidade de notas utilizadas
	    return ResponseEntity.ok(notas);
	}
	
	
}
