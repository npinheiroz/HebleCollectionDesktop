package com.example.heblecollectiondesktop.database;

import com.example.heblecollectiondesktop.model.Empresa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    /**
     * Lista apenas as empresas pendentes de aprovação (aprovado = 0 / false).
     */
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

    /**
     * NOVO: Lista apenas as empresas já APROVADAS (aprovado = 1 / true).
     * Ideal para ser exibido na tela principal de Gerenciar Empresas.
     */
    public List<Empresa> listarAprovadas() {
        String sql = "SELECT * FROM login_schema.empresa WHERE aprovado = 1 ORDER BY nome ASC";
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
            throw new RuntimeException("Erro ao listar empresas aprovadas: " + e.getMessage(), e);
        }

        return empresas;
    }

    /**
     * Lista todas as empresas do sistema (aprovadas e pendentes).
     */
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

    /**
     * NOVO: Atualiza os dados completos de uma empresa existente (nome, cnpj, estilo).
     */
    public boolean atualizar(Empresa empresa) {
        String sql = "UPDATE login_schema.empresa SET nome = ?, cnpj = ?, estilo = ? WHERE id = ?";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, empresa.getNome());
            stmt.setString(2, empresa.getCnpj());
            stmt.setString(3, empresa.getEstilo());
            stmt.setInt(4, empresa.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dados da empresa: " + e.getMessage(), e);
        }
    }

    /**
     * NOVO: Sobrecarga para atualizar campos específicos informando o ID.
     */
    public boolean atualizarCampos(int id, String nome, String cnpj, String estilo) {
        String sql = "UPDATE login_schema.empresa SET nome = ?, cnpj = ?, estilo = ? WHERE id = ?";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, cnpj);
            stmt.setString(3, estilo);
            stmt.setInt(4, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar campos da empresa: " + e.getMessage(), e);
        }
    }

    /**
     * Atualiza o status de aprovação de uma empresa no banco.
     */
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

    /**
     * Busca os dados de uma única empresa informando seu ID.
     */
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

    /**
     * Exclui uma empresa do banco pelo ID.
     */
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