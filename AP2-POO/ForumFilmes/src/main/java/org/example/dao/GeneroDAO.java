package org.example.dao;

import org.example.Util.Genero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GeneroDAO implements BaseDAO {

    private Connection connection;

    public GeneroDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void salvar(Object objeto) {
        if (!(objeto instanceof Genero)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Genero.");
        }
        Genero genero = (Genero) objeto;

        String sql = "INSERT INTO genero (nome_genero) VALUES (?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstm.setString(1, genero.getNomeGenero());
            pstm.executeUpdate();

            try (ResultSet rs = pstm.getGeneratedKeys()) {
                if (rs.next()) {
                    genero.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar gênero", e);
        }
    }

    @Override
    public Genero buscarPorId(int id) {
        String sql = "SELECT id, nome_genero FROM genero WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    Genero genero = new Genero(
                            rs.getInt("id"),
                            rs.getString("nome_genero"),
                            null // não carregamos a lista de conteudos aqui
                    );
                    return genero;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar gênero por id", e);
        }

        return null;
    }

    @Override
    public ArrayList<Object> listarTodosLazyLoading() {
        ArrayList<Object> generos = new ArrayList<>();
        String sql = "SELECT id, nome_genero FROM genero";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Genero genero = new Genero(
                            rs.getInt("id"),
                            rs.getString("nome_genero"),
                            null
                    );
                    generos.add(genero);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar gêneros", e);
        }
        return generos;
    }

    @Override
    public ArrayList<Object> listarTodosEagerLoading() {
        return null;
    }

    @Override
    public void atualizar(Object objeto) {
        if (!(objeto instanceof Genero)) {
            throw new IllegalArgumentException("Objeto deve ser do tipo Genero.");
        }
        Genero genero = (Genero) objeto;

        String sql = "UPDATE genero SET nome_genero = ? WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, genero.getNomeGenero());
            pstm.setInt(2, genero.getId());

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar gênero", e);
        }
    }

    @Override
    public void excluir(int id) {
        String sql = "DELETE FROM genero WHERE id = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setInt(1, id);
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir gênero", e);
        }
    }
}
