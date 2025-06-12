package org.example.dao;
import org.example.Util.Conteudo;
import org.example.Util.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ConteudoRepository {

    private Connection connection;
    private FilmeDAO filmeDAO;
    private SerieDAO serieDAO;

    public ConteudoRepository(Connection connection) {
        this.connection = connection;
        this.filmeDAO = new FilmeDAO(connection);
        this.serieDAO = new SerieDAO(connection);
    }

    public Conteudo buscarPorId(int id) {

        Conteudo conteudo = filmeDAO.buscarPorId(id);
        if (conteudo != null) return conteudo;


        conteudo = serieDAO.buscarPorId(id);
        if (conteudo != null) return conteudo;


        return null;
    }

    public void adicionarGeneroAoConteudo(int conteudoId, int generoId) {
        String sql = "INSERT INTO conteudo_genero (conteudo_id, genero_id) VALUES (?, ?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, conteudoId);
            pstm.setInt(2, generoId);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao adicionar gênero ao conteúdo", e);
        }
    }

    public List<Genero> listarGenerosDoConteudo(int conteudoId) {
        List<Genero> generos = new ArrayList<>();

        String sql = "SELECT g.id, g.nome_genero FROM conteudo_genero cg " +
                "INNER JOIN genero g ON cg.genero_id = g.id " +
                "WHERE cg.conteudo_id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, conteudoId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Genero genero = new Genero();
                    genero.setId(rs.getInt("id"));
                    genero.setNomeGenero(rs.getString("nome_genero"));
                    generos.add(genero);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar gêneros do conteúdo", e);
        }

        return generos;
    }




}

