package br.com.gabs.mybooks_api.controller;

import br.com.gabs.mybooks_api.model.Livro; // <-- CORRIGIDO
import br.com.gabs.mybooks_api.service.LivroService; // <-- CORRIGIDO
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "http://localhost:5173")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping
    public ResponseEntity<List<Livro>> buscarTodos() {
        List<Livro> livros = livroService.carregarLivros();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable UUID id) {
        return livroService.buscarPorId(id)
                .map(livroEncontrado -> ResponseEntity.ok(livroEncontrado))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Livro> adicionarLivro(@RequestBody Livro livro) {
        Livro novoLivro = livroService.adicionarLivro(livro);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoLivro.getId())
                .toUri();
        return ResponseEntity.created(location).body(novoLivro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable UUID id, @RequestBody Livro livroAtualizado) {
        livroAtualizado.setId(id);
        try {
            Livro livroSalvo = livroService.atualizarLivro(livroAtualizado);
            return ResponseEntity.ok(livroSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable UUID id) {
        livroService.excluirLivro(id);
        return ResponseEntity.noContent().build();
    }
}