package com.chutaai.controller;

import com.chutaai.model.entity.Partida;
import com.chutaai.model.repository.PartidaRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

// Esta URL "escuta" o clique do botão do formulário
@WebServlet("/salvarPartida")
public class SalvarPartidaServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. Pega o que o usuário digitou na tela
        String timeCasa = request.getParameter("timeCasa");
        String timeFora = request.getParameter("timeFora");
        String fase = request.getParameter("fase");

        // 2. Cria uma nova Partida e preenche os dados
        Partida novaPartida = new Partida();
        novaPartida.setTimeCasa(timeCasa);
        novaPartida.setTimeFora(timeFora);
        novaPartida.setFase(fase);
        novaPartida.setDataHora(new Date()); // Colocando a data e hora atual temporariamente

        // 3. Manda o Repository salvar no MySQL
        PartidaRepository repository = new PartidaRepository();
        repository.salvar(novaPartida);

        // 4. Recarrega a página index
        response.sendRedirect("index.jsp");
    }
}