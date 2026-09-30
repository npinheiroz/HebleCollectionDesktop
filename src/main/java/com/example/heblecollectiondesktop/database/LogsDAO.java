package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.heblecollectiondesktop.model.Funcionario;
import com.example.heblecollectiondesktop.model.LogModeracao;

public class LogsDAO {

    public boolean salvar(LogModeracao log) {
        String sql = "INSERT INTO logs_moderacao (funcionario_id, acao, alvo_afetado, detalhes, data_acao) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, log.getFuncionarioId() != null ? log.getFuncionarioId() : "SISTEMA");
            stmt.setString(2, log.getAcao() != null ? log.getAcao() : "N/A");
            stmt.setString(3, log.getAlvoAfetado() != null ? log.getAlvoAfetado() : "N/A");
            stmt.setString(4, log.getDetalhes() != null ? log.getDetalhes() : "");
            stmt.setTimestamp(5, Timestamp.valueOf(log.getDataAcao() != null ? log.getDataAcao() : LocalDateTime.now()));

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("❌ [LogsDAO] Erro ao salvar log no banco de dados: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<LogModeracao> listarTodos() {
        List<LogModeracao> logs = new ArrayList<>();
        String sql = "SELECT id, funcionario_id, acao, alvo_afetado, detalhes, data_acao FROM logs_moderacao ORDER BY data_acao DESC";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String funcionarioId = rs.getString("funcionario_id");
                String acao = rs.getString("acao");
                String alvoAfetado = rs.getString("alvo_afetado");
                String detalhes = rs.getString("detalhes");

                Timestamp ts = rs.getTimestamp("data_acao");
                LocalDateTime dataAcao = ts != null ? ts.toLocalDateTime() : null;

                logs.add(new LogModeracao(id, funcionarioId, acao, alvoAfetado, detalhes, dataAcao));
            }

        } catch (SQLException e) {
            System.err.println("❌ [LogsDAO] Erro ao listar logs: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }

    public List<LogModeracao> buscarPorModerador(String matricula) {
        List<LogModeracao> logs = new ArrayList<>();
        String sql = "SELECT id, funcionario_id, acao, alvo_afetado, detalhes, data_acao FROM logs_moderacao WHERE funcionario_id LIKE ? ORDER BY data_acao DESC";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + (matricula != null ? matricula : "") + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String funcionarioId = rs.getString("funcionario_id");
                    String acao = rs.getString("acao");
                    String alvoAfetado = rs.getString("alvo_afetado");
                    String detalhes = rs.getString("detalhes");

                    Timestamp ts = rs.getTimestamp("data_acao");
                    LocalDateTime dataAcao = ts != null ? ts.toLocalDateTime() : null;

                    logs.add(new LogModeracao(id, funcionarioId, acao, alvoAfetado, detalhes, dataAcao));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ [LogsDAO] Erro ao buscar logs por moderador: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }

    public List<LogModeracao> buscarPorAlvo(String alvo) {
        List<LogModeracao> logs = new ArrayList<>();
        String sql = "SELECT id, funcionario_id, acao, alvo_afetado, detalhes, data_acao FROM logs_moderacao WHERE alvo_afetado LIKE ? ORDER BY data_acao DESC";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + (alvo != null ? alvo : "") + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String funcionarioId = rs.getString("funcionario_id");
                    String acao = rs.getString("acao");
                    String alvoAfetado = rs.getString("alvo_afetado");
                    String detalhes = rs.getString("detalhes");

                    Timestamp ts = rs.getTimestamp("data_acao");
                    LocalDateTime dataAcao = ts != null ? ts.toLocalDateTime() : null;

                    logs.add(new LogModeracao(id, funcionarioId, acao, alvoAfetado, detalhes, dataAcao));
                }
            }

        } catch (SQLException e) {
            System.err.println("❌ [LogsDAO] Erro ao buscar logs por alvo: " + e.getMessage());
            e.printStackTrace();
        }

        return logs;
    }

    public void registrarLog(Funcionario moderador, String acao, String alvoAfetado, String detalhes) {
        String identificadorModerador = (moderador != null && moderador.getMatricula() != null && !moderador.getMatricula().isBlank())
                ? moderador.getMatricula()
                : (moderador != null && moderador.getMatricula() != null && !moderador.getMatricula().isBlank())
                ? moderador.getMatricula()
                : "SISTEMA";
        registrarLog(identificadorModerador, acao, alvoAfetado, detalhes);
    }

    public void registrarLog(String matriculaModerador, String acao, String alvoAfetado, String detalhes) {
        if (matriculaModerador == null || matriculaModerador.isBlank()) {
            matriculaModerador = "SISTEMA";
        }
        if (alvoAfetado == null || alvoAfetado.isBlank()) {
            alvoAfetado = "N/A";
        }

        LogModeracao log = new LogModeracao(0, matriculaModerador, acao, alvoAfetado, detalhes, LocalDateTime.now());
        salvar(log);
    }

    public void registrarLog(Funcionario moderador, String acao, String detalhes) {
        registrarLog(moderador, acao, "N/A", detalhes);
    }

    public void registrarLog(String matriculaModerador, String acao, String detalhes) {
        registrarLog(matriculaModerador, acao, "N/A", detalhes);
    }
}