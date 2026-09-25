package com.example.heblecollectiondesktop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.heblecollectiondesktop.model.Cupom; // Certifique-se de ter este Model criado

public class CupomDAO {

    private final String url = "jdbc:mysql://localhost:3306/login_schema?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String usuarioDb = "root";
    private final String senhaDb = "heblecollection@_2026-2027";

    public List<Cupom> listarTodos() throws SQLException {
        List<Cupom> lista = new ArrayList<>();
        String sql = "SELECT * FROM cupons ORDER BY criado_em DESC";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cupom c = new Cupom(
                        rs.getInt("id"),
                        rs.getString("codigo"),
                        rs.getDouble("desconto_percentual"),
                        rs.getBoolean("ativo"),
                        rs.getDate("validade")
                );
                lista.add(c);
            }
        }
        return lista;
    }

    public void salvar(Cupom cupom) throws SQLException {
        String sql = "INSERT INTO cupons (codigo, desconto_percentual, ativo, validade) VALUES (?, ?, ?, ?)";

        try (Connection conexao = DriverManager.getConnection(url, usuarioDb, senhaDb);
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, cupom.getCodigo());
            stmt.setDouble(2, cupom.getDescontoPercentual());
            stmt.setBoolean(3, cupom.isAtivo());
            stmt.setDate(4, new java.sql.Date(cupom.getValidade().getTime()));
            stmt.executeUpdate();
        }
    }
}