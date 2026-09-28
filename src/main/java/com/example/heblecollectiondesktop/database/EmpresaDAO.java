package com.example.heblecollectiondesktop.database;

import com.example.heblecollectiondesktop.database.conexaoDB;
import com.example.heblecollectiondesktop.model.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public List<Empresa> listarPendentes() {
        String sql = "SELECT * FROM login_schema.empresa WHERE aprovado = 0 ORDER BY id DESC";
        List<Empresa> empresas = new ArrayList<>();

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empresa e = new Empresa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("estilo"),
                        rs.getBoolean("aprovado")
                );
                empresas.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar empresas pendentes: " + e.getMessage(), e);
        }

        return empresas;
    }

    public List<Empresa> listarTodas() {
        String sql = "SELECT * FROM login_schema.empresa ORDER BY nome ASC";
        List<Empresa> empresas = new ArrayList<>();

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Empresa e = new Empresa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("estilo"),
                        rs.getBoolean("aprovado")
                );
                empresas.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as empresas: " + e.getMessage(), e);
        }

        return empresas;
    }

    public boolean atualizarStatusAprovacao(int id, boolean aprovado) {
        String sql = "UPDATE login_schema.empresa SET aprovado = ? WHERE id = ?";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, aprovado);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status da empresa: " + e.getMessage(), e);
        }
    }

    public Empresa buscarPorId(int id) {
        String sql = "SELECT * FROM login_schema.empresa WHERE id = ?";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            rs.getString("estilo"),
                            rs.getBoolean("aprovado")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar empresa por ID: " + e.getMessage(), e);
        }

        return null;
    }

    public boolean deletar(int id) {
        String sql = "DELETE FROM login_schema.empresa WHERE id = ?";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar empresa: " + e.getMessage(), e);
        }
    }
}