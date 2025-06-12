package org.example.dao;
import org.example.Util.Serie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class SerieDAO implements BaseDAO {

    private Connection connection;

    public SerieDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Serie> listarSeriesComMinimoSeisEpisodios() {
        List<Serie> series = new ArrayList<>();

        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, s.episodios " +
                "FROM conteudo c " +
                "JOIN serie s ON c.id = s.conteudo_id " +
                "WHERE s.episodios >= 6";

        try (PreparedStatement pstm = connection.prepareStatement(sql);
             ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Serie serie = new Serie(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getInt("lancamento"),
                        null, // generos
                        rs.getString("diretor"),
                        rs.getInt("classificacao"),
                        rs.getInt("episodios")
                );
                series.add(serie);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar séries com no mínimo 6 episódios", e);
        }

        return series;
    }


    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Serie)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Serie.");
        }
        Serie serie = (Serie) objeto;

        try {
            connection.setAutoCommit(false);

            // Insere na tabela conteudo
            String sqlConteudo = "INSERT INTO conteudo (titulo, lancamento, diretor, classificacao) VALUES (?, ?, ?, ?)";

            try (PreparedStatement pstmConteudo = connection.prepareStatement(sqlConteudo, Statement.RETURN_GENERATED_KEYS)) {
                pstmConteudo.setString(1, serie.getTitulo());
                pstmConteudo.setInt(2, serie.getLancamento());
                pstmConteudo.setString(3, serie.getDiretor());
                pstmConteudo.setInt(4, serie.getClassificacao());

                pstmConteudo.executeUpdate();

                try (ResultSet rs = pstmConteudo.getGeneratedKeys()) {
                    if (rs.next()) {
                        int conteudoId = rs.getInt(1);
                        serie.setId(conteudoId);

                        // Insere na tabela serie
                        String sqlSerie = "INSERT INTO serie (conteudo_id, episodios) VALUES (?, ?)";

                        try (PreparedStatement pstmSerie = connection.prepareStatement(sqlSerie)) {
                            pstmSerie.setInt(1, conteudoId);
                            pstmSerie.setInt(2, serie.getEpisodios());

                            pstmSerie.executeUpdate();
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
            throw new RuntimeException("Erro ao salvar série", e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Serie buscarPorId(int id) {
        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, s.episodios " +
                "FROM conteudo c JOIN serie s ON c.id = s.conteudo_id WHERE c.id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Serie serie = new Serie(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getInt("lancamento"),
                            null, // generos podem ser carregados depois
                            rs.getString("diretor"),
                            rs.getInt("classificacao"),
                            rs.getInt("episodios")
                    );
                    return serie;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar série por ID", e);
        }
        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> lista = new ArrayList<>();
        String sql = "SELECT c.id, c.titulo, c.lancamento, c.diretor, c.classificacao, s.episodios " +
                "FROM conteudo c JOIN serie s ON c.id = s.conteudo_id";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Serie serie = new Serie(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getInt("lancamento"),
                            null,
                            rs.getString("diretor"),
                            rs.getInt("classificacao"),
                            rs.getInt("episodios")
                    );
                    lista.add(serie);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar séries", e);
        }
        return lista;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return null;
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Serie)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Serie.");
        }
        Serie serie = (Serie) objeto;

        try {
            connection.setAutoCommit(false);

            // Atualiza tabela conteudo
            String sqlConteudo = "UPDATE conteudo SET titulo = ?, lancamento = ?, diretor = ?, classificacao = ? WHERE id = ?";
            try (PreparedStatement pstmConteudo = connection.prepareStatement(sqlConteudo)) {
                pstmConteudo.setString(1, serie.getTitulo());
                pstmConteudo.setInt(2, serie.getLancamento());
                pstmConteudo.setString(3, serie.getDiretor());
                pstmConteudo.setInt(4, serie.getClassificacao());
                pstmConteudo.setInt(5, serie.getId());

                pstmConteudo.executeUpdate();
            }

            // Atualiza tabela serie
            String sqlSerie = "UPDATE serie SET episodios = ? WHERE conteudo_id = ?";
            try (PreparedStatement pstmSerie = connection.prepareStatement(sqlSerie)) {
                pstmSerie.setInt(1, serie.getEpisodios());
                pstmSerie.setInt(2, serie.getId());

                pstmSerie.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Erro ao atualizar série", e);
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
            throw new RuntimeException("Erro ao excluir série", e);
        }
    }
}
