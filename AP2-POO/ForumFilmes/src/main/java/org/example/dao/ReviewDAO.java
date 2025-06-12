package org.example.dao;

import org.example.Util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ReviewDAO implements BaseDAO {

    private Connection connection;

    public ReviewDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Review)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Review.");
        }
        Review review = (Review) objeto;

        String sql = "INSERT INTO review (usuario_id, conteudo_id, nota, titulo, comentario) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, review.getAutor().getId());
            pstm.setInt(2, review.getConteudo().getId());
            pstm.setFloat(3, review.getNota());
            pstm.setString(4, review.getTitulo());
            pstm.setString(5, review.getComentario());

            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    review.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar review", e);
        }
    }

    @Override
    public Review buscarPorId(int id) {
        String sql = "SELECT r.id, r.usuario_id, r.conteudo_id, r.nota, r.titulo, r.comentario, " +
                "u.username, c.titulo AS conteudo_titulo " +
                "FROM review r " +
                "JOIN usuario u ON r.usuario_id = u.id " +
                "JOIN conteudo c ON r.conteudo_id = c.id " +
                "WHERE r.id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Usuario autor = new Usuario();
                    autor.setId(rs.getInt("usuario_id"));
                    autor.setUsername(rs.getString("username"));

                    Conteudo conteudo = new Conteudo() {
                        @Override
                        public void exibirInfo() {

                        }
                    };
                    conteudo.setId(rs.getInt("conteudo_id"));
                    conteudo.setTitulo(rs.getString("conteudo_titulo"));

                    Review review = new Review(
                            rs.getInt("id"),
                            autor,
                            conteudo,
                            rs.getFloat("nota"),
                            rs.getString("titulo"),
                            rs.getString("comentario")
                    );
                    return review;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar review por id", e);
        }
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> reviews = new ArrayList<>();

        String sql = "SELECT r.id, r.usuario_id, r.conteudo_id, r.nota, r.titulo, r.comentario, " +
                "u.username, c.titulo AS conteudo_titulo, c.tipo, c.lancamento, c.diretor, c.classificacao, " +
                "f.duracao, s.episodios " +
                "FROM review r " +
                "JOIN usuario u ON r.usuario_id = u.id " +
                "JOIN conteudo c ON r.conteudo_id = c.id " +
                "LEFT JOIN filme f ON c.id = f.conteudo_id " +
                "LEFT JOIN serie s ON c.id = s.conteudo_id";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Usuario autor = new Usuario();
                autor.setId(rs.getInt("usuario_id"));
                autor.setUsername(rs.getString("username"));


                Conteudo conteudo = null;
                String tipo = rs.getString("tipo");
                int conteudoId = rs.getInt("conteudo_id");
                String conteudoTitulo = rs.getString("conteudo_titulo");
                int lancamento = rs.getInt("lancamento");
                String diretor = rs.getString("diretor");
                int classificacao = rs.getInt("classificacao");

                if ("filme".equalsIgnoreCase(tipo)) {
                    int duracao = rs.getInt("duracao");
                    conteudo = new Filme(conteudoId, conteudoTitulo, lancamento, null, diretor, classificacao, duracao);
                } else if ("serie".equalsIgnoreCase(tipo)) {
                    int episodios = rs.getInt("episodios");
                    conteudo = new Serie(conteudoId, conteudoTitulo, lancamento, null, diretor, classificacao, episodios);
                }

                // Cria o Review
                Review review = new Review(
                        rs.getInt("id"),
                        autor,
                        conteudo,
                        rs.getFloat("nota"),
                        rs.getString("titulo"),
                        rs.getString("comentario")
                );

                reviews.add(review);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar reviews", e);
        }

        return reviews;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return null;
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Review)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Review.");
        }
        Review review = (Review) objeto;

        String sql = "UPDATE review SET usuario_id = ?, conteudo_id = ?, nota = ?, titulo = ?, comentario = ? WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, review.getAutor().getId());
            pstm.setInt(2, review.getConteudo().getId());
            pstm.setFloat(3, review.getNota());
            pstm.setString(4, review.getTitulo());
            pstm.setString(5, review.getComentario());
            pstm.setInt(6, review.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar review", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM review WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir review", e);
        }
    }
}
