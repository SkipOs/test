package ifsp.edu.source.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ifsp.edu.source.Model.Conta;
import ifsp.edu.source.Model.Conta.statusConta;
import ifsp.edu.source.Model.Conta.tipoConta;
import ifsp.edu.source.Model.Usuario;


public class DaoConta {
	
	
	public long incluir(Conta v) {
        DataBaseCom.conectar();

        String sqlString = "insert into conta(tipo, status, valor, numero_conta, situacao) values (?,?,?,?,?)";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString, PreparedStatement.RETURN_GENERATED_KEYS);

            // Converte os enums para String
            ps.setString(1, v.getTipoConta().name());
            ps.setString(2, v.getStatusConta().name());
            ps.setDouble(3, v.getValor());
            ps.setString(4, v.getNumeroConta());
            ps.setString(5, "ATIVA");

            ps.executeUpdate();

            // Recupera o ID gerado
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1); // Retorna o ID gerado
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1; // Retorna -1 se houver erro
    }
	
	public boolean verificarNumeroContaExistente(String numeroConta) {
        DataBaseCom.conectar();
        String sqlString = "SELECT COUNT(*) FROM conta WHERE numero_conta = ?";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, numeroConta);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Retorna true se o número já existir
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
	
	
	
	public Conta buscarContaPorNumero(String numeroConta) {
	    DataBaseCom.conectar();
	    Conta p = null;
	    String sql = "SELECT * FROM conta WHERE numero_conta = ?";
	    try {
	        PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sql);
	        ps.setString(1, numeroConta);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            p = new Conta();
	            p.setId(rs.getLong("id"));
	            p.setNumeroConta(rs.getString("numero_conta"));
	            p.setTipoConta(tipoConta.valueOf(rs.getString("tipo")));  // Se `tipoConta` for um enum
	            p.setStatusConta(statusConta.valueOf(rs.getString("status")));
	            p.setValor(rs.getDouble("valor"));
	            p.setSituacao(rs.getString("situacao"));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return p;
	}

	
	
	public Conta buscarContaPorId(long idConta) {
	    DataBaseCom.conectar();
	    Conta conta = null;
	    try {
	        String query = "SELECT * FROM conta WHERE id = ?";
	        PreparedStatement stmt = DataBaseCom.getConnection().prepareStatement(query);
	        stmt.setLong(1, idConta);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            conta = new Conta();
	            conta.setId(rs.getLong("id"));
	            conta.setNumeroConta(rs.getString("numero_conta"));
	            conta.setTipoConta(tipoConta.valueOf(rs.getString("tipo")));  // Se `tipoConta` for um enum
	            conta.setStatusConta(statusConta.valueOf(rs.getString("status")));
	            conta.setValor(rs.getDouble("valor"));
	            conta.setSituacao(rs.getString("situacao"));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	    return conta;
	}
	
	public void atualizarConta(Conta conta) {
	    DataBaseCom.conectar();
	    try {
	        String query = "UPDATE conta SET valor = ? WHERE id = ?";
	        PreparedStatement stmt = DataBaseCom.getConnection().prepareStatement(query);

	        // Definindo os valores no PreparedStatement
	        // Converte o enum para String
	        stmt.setDouble(1, conta.getValor());
	        stmt.setLong(2, conta.getId());

	        // Executa a atualização no banco de dados
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } 
	}
	
	public boolean inativarConta(Conta conta) {
	    DataBaseCom.conectar();
	    
	    String sqlString = "UPDATE conta SET situacao = ? WHERE numero_conta = ?";
	    
	    try {
	        PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
	        ps.setString(1, conta.getSituacao());
	        ps.setString(2, conta.getNumeroConta());
	        
	        int resultado = ps.executeUpdate();
	        return resultado > 0;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    
	    return false;
	}



}
