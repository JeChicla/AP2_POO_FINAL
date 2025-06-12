package org.example.dao;
import org.example.Util.Conteudo;
import org.example.Util.Filme;
import org.example.Util.Serie;
import org.example.Util.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements BaseDAO {

    private Connection connection;

    public UsuarioDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario usuario = (Usuario) objeto;

        String sql = "INSERT INTO usuario (username) VALUES (?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, usuario.getUsername());

            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    public void adicionarConteudoAoUsuario(int usuarioId, int conteudoId) {
        String sql = "INSERT INTO usuario_conteudo_adicionado (usuario_id, conteudo_id) VALUES (?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            pstm.setInt(2, conteudoId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar conteúdo ao usuário", e);
        }
    }
    public List<Conteudo> listarConteudosDoUsuario(int usuarioId) {
        List<Conteudo> conteudos = new ArrayList<>();

        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, " +
                "c.tipo, f.duracao AS filme_duracao, s.episodios AS serie_episodios " +
                "FROM usuario_conteudo_adicionado uca " +
                "JOIN conteudo c ON uca.conteudo_id = c.id " +
                "LEFT JOIN filme f ON c.id = f.id " +
                "LEFT JOIN serie s ON c.id = s.id " +
                "WHERE uca.usuario_id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Conteudo conteudo;

                    if (rs.getObject("filme_duracao") != null) {
                        // É um filme
                        conteudo = new Filme();
                        ((Filme) conteudo).setDuracao(rs.getInt("filme_duracao"));
                    } else if (rs.getObject("serie_episodios") != null) {
                        // É uma série
                        conteudo = new Serie();
                        ((Serie) conteudo).setEpisodios(rs.getInt("serie_episodios"));
                    } else {
                        continue; // Tipo não identificado
                    }

                    conteudo.setId(rs.getInt("id"));
                    conteudo.setTitulo(rs.getString("titulo"));
                    conteudo.setLancamento(rs.getInt("lancamento"));
                    conteudo.setDiretor(rs.getString("diretor"));
                    conteudo.setClassificacao(rs.getInt("classificacao"));

                    conteudos.add(conteudo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar conteúdos do usuário", e);
        }

        return conteudos;
    }




    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT id, username FROM usuario WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário por ID", e);
        }

        return null;
    }

    public List<Usuario> listarUsuariosQueAdicionaramFilmes() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT u.id, u.username " +
                "FROM usuario u " +
                "INNER JOIN usuario_conteudo_adicionado uca ON u.id = uca.usuario_id " +
                "INNER JOIN filme f ON uca.conteudo_id = f.conteudo_id";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários que adicionaram filmes", e);
        }

        return usuarios;
    }

    public List<Usuario> listarUsuarios2seguidores() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT u.id, u.username, COUNT(us.seguidor_id) AS total_seguidores " +
                "FROM usuario u " +
                "JOIN usuario_seguidores us ON u.id = us.seguido_id " +
                "GROUP BY u.id, u.username " +
                "HAVING COUNT(us.seguidor_id) >= 2";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários com pelo menos 2 seguidores", e);
        }

        return usuarios;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> usuarios = new ArrayList<>();

        String sql = "SELECT id, username FROM usuario";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuarios.add(usuario);
            }

            return usuarios;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        // Eager loading seria ideal se você quiser carregar seguidores, favoritos etc.
        // Aqui faremos o mesmo que o lazy por simplicidade.
        return listarTodosLazyLoading();
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Usuario)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Usuario.");
        }

        Usuario usuario = (Usuario) objeto;

        String sql = "UPDATE usuario SET username = ? WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, usuario.getUsername());
            pstm.setInt(2, usuario.getId());

            int linhasAfetadas = pstm.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao atualizar: nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar usuário", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM usuario WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            int linhasAfetadas = pstm.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao excluir: nenhuma linha foi afetada.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir usuário", e);
        }
    }

    public void adicionarFavoritoAoUsuario(int usuarioId, int conteudoId) {
        String sql = "INSERT INTO usuario_favoritos (usuario_id, conteudo_id) VALUES (?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            pstm.setInt(2, conteudoId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar conteúdo favorito ao usuário", e);
        }
    }

    public List<Conteudo> listarFavoritosDoUsuario(int usuarioId) {
        List<Conteudo> favoritos = new ArrayList<>();

        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, " +
                "f.duracao AS filme_duracao, s.episodios AS serie_episodios " +
                "FROM usuario_favoritos uf " +
                "JOIN conteudo c ON uf.conteudo_id = c.id " +
                "LEFT JOIN filme f ON c.id = f.id " +
                "LEFT JOIN serie s ON c.id = s.id " +
                "WHERE uf.usuario_id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Conteudo conteudo;

                    if (rs.getObject("filme_duracao") != null) {
                        conteudo = new Filme();
                        ((Filme) conteudo).setDuracao(rs.getInt("filme_duracao"));
                    } else if (rs.getObject("serie_episodios") != null) {
                        conteudo = new Serie();
                        ((Serie) conteudo).setEpisodios(rs.getInt("serie_episodios"));
                    } else {
                        continue; // Tipo indefinido
                    }

                    conteudo.setId(rs.getInt("id"));
                    conteudo.setTitulo(rs.getString("titulo"));
                    conteudo.setLancamento(rs.getInt("lancamento"));
                    conteudo.setDiretor(rs.getString("diretor"));
                    conteudo.setClassificacao(rs.getInt("classificacao"));

                    favoritos.add(conteudo);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar conteúdos favoritos do usuário", e);
        }

        return favoritos;
    }

    public void seguirUsuario(int seguidorId, int seguidoId) {
        if (seguidorId == seguidoId) {
            throw new IllegalArgumentException("Um usuário não pode seguir a si mesmo.");
        }

        String sql = "INSERT INTO usuario_seguidores (seguidor_id, seguido_id) VALUES (?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, seguidorId);
            pstm.setInt(2, seguidoId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao seguir usuário", e);
        }
    }

    public List<Usuario> listarSeguidores(int usuarioId) {
        List<Usuario> seguidores = new ArrayList<>();

        String sql = "SELECT u.id, u.username " +
                "FROM usuario_seguidores us " +
                "JOIN usuario u ON us.seguidor_id = u.id " +
                "WHERE us.seguido_id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, usuarioId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Usuario seguidor = new Usuario();
                    seguidor.setId(rs.getInt("id"));
                    seguidor.setUsername(rs.getString("username"));
                    seguidores.add(seguidor);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar seguidores", e);
        }

        return seguidores;
    }
    public List<Usuario> listarUsuariosCom1Conteudo() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT u.id, u.username " +
                "FROM usuario u " +
                "JOIN usuario_conteudo_adicionado uca ON u.id = uca.usuario_id " +
                "GROUP BY u.id, u.username " +
                "HAVING COUNT(uca.conteudo_id) = 1";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setUsername(rs.getString("username"));
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários com exatamente 1 conteúdo", e);
        }

        return usuarios;
    }


}

