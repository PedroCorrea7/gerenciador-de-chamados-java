package br.com.dunnastecnologia.chamados.infrastructure.service;

import br.com.dunnastecnologia.chamados.domain.model.AreaComum;
import br.com.dunnastecnologia.chamados.infrastructure.repository.AreaComumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AreaComumService {

    private final AreaComumRepository repository;

    public AreaComumService(AreaComumRepository repository) {
        this.repository = repository;
    }

    public List<AreaComum> listarTodas() {
        return repository.findAll();
    }

    public List<AreaComum> listarAtivas() {
        return repository.findAllByAtivaTrue();
    }

    @Transactional
    public AreaComum salvar(String nome, String descricao) {
        AreaComum area = new AreaComum();
        area.setNome(nome);
        area.setDescricao(descricao);
        area.setAtiva(true);
        return repository.save(area);
    }

    @Transactional
    public void alternarStatus(UUID id) {
        AreaComum area = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Area nno encontrada."));

        area.setAtiva(!area.getAtiva());
        repository.save(area);
    }
}
