package org.example.dao;
import org.example.Util.Filme;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FilmeDAO implements BaseDAO {

    private Connection connection;

    public FilmeDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Filme)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Filme.");
        }
        Filme filme = (Filme) objeto;

        try {
            connection.setAutoCommit(false);

            // Inserir na tabela conteudo
            String sqlConteudo = "INSERT INTO conteudo (titulo, lancamento, diretor, classificacao) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmConteudo = connection.prepareStatement(sqlConteudo, Statement.RETURN_GENERATED_KEYS)) {
                pstmConteudo.setString(1, filme.getTitulo());
                pstmConteudo.setInt(2, filme.getLancamento());
                pstmConteudo.setString(3, filme.getDiretor());
                pstmConteudo.setInt(4, filme.getClassificacao());

                pstmConteudo.executeUpdate();

                try (ResultSet rs = pstmConteudo.getGeneratedKeys()) {
                    if (rs.next()) {
                        int conteudoId = rs.getInt(1);
                        filme.setId(conteudoId);

                        // Inserir na tabela filme
                        String sqlFilme = "INSERT INTO filme (conteudo_id, duracao) VALUES (?, ?)";

                        try (PreparedStatement pstmFilme = connection.prepareStatement(sqlFilme)) {
                            pstmFilme.setInt(1, conteudoId);
                            pstmFilme.setInt(2, filme.getDuracao());

                            pstmFilme.executeUpdate();
                        }
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Filme buscarPorId(int id) {
        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, f.duracao " +
                "FROM conteudo c JOIN filme f ON c.id = f.conteudo_id WHERE c.id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Filme filme = new Filme(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getInt("lancamento"),
                            null, // generos - pode carregar depois
                            rs.getString("diretor"),
                            rs.getInt("classificacao"),
                            rs.getInt("duracao")
                    );
                    return filme;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, f.duracao " +
                "FROM conteudo c JOIN filme f ON c.id = f.conteudo_id";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Filme filme = new Filme(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getInt("lancamento"),
                            null,
                            rs.getString("diretor"),
                            rs.getInt("classificacao"),
                            rs.getInt("duracao")
                    );
                    lista.add(filme);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lista;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return null;
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Filme)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Filme.");
        }
        Filme filme = (Filme) objeto;

        try {
            connection.setAutoCommit(false);

            // Atualizar tabela conteudo
            String sqlConteudo = "UPDATE conteudo SET titulo = ?, lancamento = ?, diretor = ?, classificacao = ? WHERE id = ?";
            try (PreparedStatement pstmConteudo = connection.prepareStatement(sqlConteudo)) {
                pstmConteudo.setString(1, filme.getTitulo());
                pstmConteudo.setInt(2, filme.getLancamento());
                pstmConteudo.setString(3, filme.getDiretor());
                pstmConteudo.setInt(4, filme.getClassificacao());
                pstmConteudo.setInt(5, filme.getId());

                pstmConteudo.executeUpdate();
            }

            // Atualizar tabela filme
            String sqlFilme = "UPDATE filme SET duracao = ? WHERE conteudo_id = ?";
            try (PreparedStatement pstmFilme = connection.prepareStatement(sqlFilme)) {
                pstmFilme.setInt(1, filme.getDuracao());
                pstmFilme.setInt(2, filme.getId());

                pstmFilme.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM conteudo WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}