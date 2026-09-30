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


    public boolean salvar(LogModeracao log) throws SQLException {
        String sql = "INSERT INTO logs_moderacao (funcionario_id, acao, detalhes, data_acao) VALUES (?, ?, ?, ?)";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, log.getFuncionarioId());
            stmt.setString(2, log.getAcao());
            stmt.setString(3, log.getDetalhes());
            stmt.setTimestamp(4, Timestamp.valueOf(log.getDataAcao() != null ? log.getDataAcao() : LocalDateTime.now()));

            return stmt.executeUpdate() > 0;
        }
    }

    /**
     * Retorna a lista completa de logs ordenados do mais recente para o mais antigo.
     */
    public List<LogModeracao> listarTodos() throws SQLException {
        List<LogModeracao> logs = new ArrayList<>();
        String sql = "SELECT id, funcionario_id, acao, detalhes, data_acao FROM logs_moderacao ORDER BY data_acao DESC";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String funcionarioId = rs.getString("funcionario_id");
                String acao = rs.getString("acao");
                String detalhes = rs.getString("detalhes");

                Timestamp ts = rs.getTimestamp("data_acao");
                LocalDateTime dataAcao = ts != null ? ts.toLocalDateTime() : null;

                logs.add(new LogModeracao(id, funcionarioId, acao, detalhes, dataAcao));
            }
        }

        return logs;
    }

    public void registrarLog(Funcionario moderador, String acao, String detalhes) {
        if (moderador == null) {
            System.err.println("Aviso: Nenhum funcionário logado foi passado para registrar o log.");
            return;
        }

        String identificadorModerador = String.valueOf(moderador.getMatricula());

        try {
            LogModeracao log = new LogModeracao(0, identificadorModerador, acao, detalhes, LocalDateTime.now());
            salvar(log);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Falha ao gravar log de moderação: " + e.getMessage());
        }
    }

    public void registrarLog(String matriculaModerador, String acao, String detalhes) {
        if (matriculaModerador == null || matriculaModerador.isBlank()) {
            System.err.println("Aviso: Matrícula inválida informada para o log.");
            return;
        }

        try {
            LogModeracao log = new LogModeracao(0, matriculaModerador, acao, detalhes, LocalDateTime.now());
            salvar(log);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Falha ao gravar log de moderação: " + e.getMessage());
        }
    }

    public List<LogModeracao> buscarPorModerador(String matricula) throws SQLException {
        List<LogModeracao> logs = new ArrayList<>();
        String sql = "SELECT id, funcionario_id, acao, detalhes, data_acao FROM logs_moderacao"
                + "WHERE funcionario_id LIKE ? ORDER BY data_acao DESC";

        try (Connection conn = conexaoDB.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + matricula + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String funcionarioId = rs.getString("funcionario_id");
                    String acao = rs.getString("acao");
                    String detalhes = rs.getString("detalhes");

                    Timestamp ts = rs.getTimestamp("data_acao");
                    LocalDateTime dataAcao = ts != null ? ts.toLocalDateTime() : null;

                    logs.add(new LogModeracao(id, funcionarioId, acao, detalhes, dataAcao));
                }
            }
        }

        return logs;
    }
}