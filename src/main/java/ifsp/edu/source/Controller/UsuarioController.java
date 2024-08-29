package ifsp.edu.source.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.source.DAO.DaoConta;
import ifsp.edu.source.DAO.DaoUsuario;
import ifsp.edu.source.Model.Conta;
import ifsp.edu.source.Model.Usuario;
import ifsp.edu.source.Request.AtualizarUsuarioRequest;
import ifsp.edu.source.Request.ExcluirContaRequest;
import ifsp.edu.source.Response.ContaUsuarioResponse;

@RestController
public class UsuarioController {

    DaoUsuario cadUsuario = new DaoUsuario();
    DaoConta cadConta = new DaoConta();

    @GetMapping("/detalhes-usuario")
    public ResponseEntity<Map<String, Object>> obterDetalhesUsuario(@RequestParam String numeroConta) {
        Conta conta = cadConta.buscarContaPorNumero(numeroConta);
        if (conta != null) {
            long idConta = conta.getId();
            Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);
            if (usuario != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("nome", usuario.getNome());
                response.put("email", usuario.getEmail());
                response.put("cpf", usuario.getCpf());
                response.put("dataNascimento", usuario.getDataNascimento());
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Conta ou usuário não encontrados"));
    }

    @PostMapping("/atualizar")
    public ResponseEntity<String> atualizarUsuario(@RequestBody AtualizarUsuarioRequest request) {
        Conta conta = cadConta.buscarContaPorNumero(request.getNumeroConta());
        if (conta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }

        long idConta = conta.getId();
        Usuario usuario = cadUsuario.buscarUsuarioPorIdConta(idConta);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

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
            usuario.setDataNascimento(request.getDataNascimento());
            atualizado = true;
        }

        if (atualizado) {
            cadUsuario.atualizar(usuario);
            return ResponseEntity.ok("Usuário atualizado com sucesso");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhum dado para atualizar");
    }

    @PostMapping("/excluir")
    public ResponseEntity<String> excluirConta(@RequestBody ExcluirContaRequest request) {
        Conta conta = cadConta.buscarContaPorNumero(request.getNumeroConta());
        if (conta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Conta não encontrada");
        }

        double saldo = conta.getValor();
        if (saldo > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Saldo positivo de " + saldo + " encontrado. Por favor, saque o valor antes de encerrar a conta.");
        }

        conta.setSituacao("INATIVA");
        cadConta.inativarConta(conta);
        return ResponseEntity.ok(Map.of("message", "A conta foi inativada com sucesso."));
    }
}
