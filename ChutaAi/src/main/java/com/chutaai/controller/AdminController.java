package com.chutaai.controller;

import com.chutaai.model.entity.Partida;
import com.chutaai.model.entity.Usuario;
import com.chutaai.service.PalpiteService;
import com.chutaai.service.PartidaService;
import com.chutaai.service.UsuarioService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class AdminController implements Serializable {

    @Inject
    private PartidaService partidaService;

    @Inject
    private UsuarioService usuarioService;

    private Partida partidaForm = new Partida();
    private List<Partida> todasPartidas;
    private List<Usuario> todosUsuarios;

    @Inject
    private PalpiteService palpiteService; // Certifique-se de injetar o serviço de palpites


    @PostConstruct
    public void init() {
        carregarDados();
    }

    public void carregarDados() {
        this.todasPartidas = partidaService.listarTodas();
        this.todosUsuarios = usuarioService.listarTodos();
    }

    // --- GESTÃO DE PARTIDAS ---

    public void salvarPartida() {
        try {
            partidaService.salvar(partidaForm);

            // SE O RESULTADO FOI LANÇADO, CALCULA OS PONTOS NA HORA
            if (partidaForm.getPlacarCasaReal() != null) {
                palpiteService.processarPontosDaPartida(partidaForm);
            }

            partidaForm = new Partida();
            carregarDados();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void prepararEdicao(Partida p) {
        this.partidaForm = p;
    }

    public void excluirPartida(Partida p) {
        try {
            partidaService.excluir(p);
            carregarDados();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Excluído", "Partida removida com sucesso."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Não é possível excluir partidas com palpites ativos."));
        }
    }

    // --- GESTÃO DE USUÁRIOS ---

    public void excluirUsuario(Usuario u) {
        if (u.isAdmin()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Você não pode excluir um administrador."));
            return;
        }
        usuarioService.excluir(u);
        carregarDados();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Usuário removido do bolão."));
    }

    // Getters e Setters
    public Partida getPartidaForm() { return partidaForm; }
    public void setPartidaForm(Partida partidaForm) { this.partidaForm = partidaForm; }
    public List<Partida> getTodasPartidas() { return todasPartidas; }
    public List<Usuario> getTodosUsuarios() { return todosUsuarios; }
}