package ifsp.edu.source.DAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ifsp.edu.source.Model.Usuario;





public class DaoUsuario {
	
	
	public boolean incluir(Usuario v) {
    DataBaseCom.conectar();

    String sqlString = "INSERT INTO usuario (nome, senha, id_conta, salario, email, cpf, data_nascimento) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
        ps.setString(1, v.getNome());
        ps.setString(2, v.getSenha());
        ps.setLong(3, v.getIdConta());
        ps.setDouble(4, v.getSalario());
        ps.setString(5, v.getEmail());
        ps.setString(6, v.getCpf());
        ps.setDate(7, v.getDataNascimento());  // Corrija para usar Date

        int ri = ps.executeUpdate(); 
        return ri > 0; // Verifique se a inserção foi bem-sucedida
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return false;
}

	
	

	public Usuario buscarUsuarioPorIdConta(long id) {
    DataBaseCom.conectar();
    Usuario usuario = null;
    String query = "SELECT * FROM usuario WHERE id_conta = ?";

    try {
        PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(query);
        ps.setLong(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setId(rs.getLong("id"));
            usuario.setNome(rs.getString("nome"));
            usuario.setSenha(rs.getString("senha"));
            usuario.setIdConta(rs.getLong("id_conta"));
            usuario.setSalario(rs.getDouble("salario"));
            usuario.setEmail(rs.getString("email"));
            usuario.setCpf(rs.getString("cpf"));
            usuario.setDataNascimento(rs.getDate("data_nascimento").toLocalDate()); // Corrija a recuperação da data
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return usuario;
}


	
	
	public boolean atualizar(Usuario usuario) {
	    DataBaseCom.conectar();
	    
	    StringBuilder sqlString = new StringBuilder("UPDATE usuario SET ");
	    List<Object> parametros = new ArrayList<>();
	    
	    if (usuario.getNome() != null) {
	        sqlString.append("nome = ?, ");
	        parametros.add(usuario.getNome());
	    }
	    
	    if (usuario.getSenha() != null) {
	        sqlString.append("senha = ?, ");
	        parametros.add(usuario.getSenha());
	    }
	    
	    if (usuario.getEmail() != null) {
	        sqlString.append("email = ?, ");
	        parametros.add(usuario.getEmail());
	    }
	    
	    if (usuario.getCpf() != null) {
	        sqlString.append("cpf = ?, ");
	        parametros.add(usuario.getCpf());
	    }
	    
	    if (usuario.getDataNascimento() != null) {
	        sqlString.append("data_nascimento = ?, ");
	        parametros.add(usuario.getDataNascimento());
	    }
	    
	    // Remove a última vírgula e espaço
	    sqlString.setLength(sqlString.length() - 2);
	    sqlString.append(" WHERE id = ?");
	    parametros.add(usuario.getId());
	    
	    try {
	        PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString.toString());
	        
	        for (int i = 0; i < parametros.size(); i++) {
	            ps.setObject(i + 1, parametros.get(i));
	        }
	        
	        int resultado = ps.executeUpdate();
	        return resultado > 0;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    
	    return false;
	}

	

}
