package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.heblecollectiondesktop.model.Ticket; // Certifique-se de ter este Model criado

public class TicketDAO {

    private final String url = "jdbc:mysql://localhost:3306/login_schema?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String usuarioDb = "root";
    private final String senhaDb = "heblecollection@_2026-2027";

    public List<Ticket> listarTodos() throws SQLException {
        List<Ticket> lista = new ArrayList<>();
        String sql = "SELECT * FROM tickets ORDER BY criado_em DESC";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Ticket t = new Ticket(
                        rs.getInt("id"),
                        rs.getString("assunto"),
                        rs.getString("descricao"),
                        rs.getString("status"),
                        rs.getInt("funcionario_id")
                );
                lista.add(t);
            }
        }
        return lista;
    }

    public void atualizarStatus(int idTicket, String novoStatus) throws SQLException {
        String sql = "UPDATE tickets SET status = ? WHERE id = ?";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, novoStatus);
            stmt.setInt(2, idTicket);
            stmt.executeUpdate();
        }
    }
}