package br.com.seunome.mybooksapi.controller;

import br.com.seunome.mybooksapi.model.Livro;
import br.com.seunome.mybooksapi.service.LivroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

// @RestController: Marca esta classe como um controller de API REST.
// O Spring Boot saberá que os retornos dos métodos devem ser convertidos para JSON.
@RestController
// @RequestMapping: Define a URL base para todos os endpoints nesta classe.
// Todas as requisições começarão com "/api/livros".
@RequestMapping("/api/livros")
// @CrossOrigin: Permite que seu frontend React (que rodará em um endereço diferente, ex: localhost:3000)
// possa fazer requisições para esta API (que rodará em localhost:8080). Essencial para o desenvolvimento.
@CrossOrigin
public class LivroController {

    // Injeção de Dependência: O controller não cria o serviço, ele o recebe do Spring.
    // Isso mantém o código desacoplado e fácil de testar.
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    // --- ENDPOINTS DA API ---

    // GET /api/livros
    // Retorna a lista de todos os livros.
    @GetMapping
    public ResponseEntity<List<Livro>> buscarTodos() {
        List<Livro> livros = livroService.carregarLivros();
        return ResponseEntity.ok(livros);
    }

    // GET /api/livros/{id}
    // Retorna um livro específico pelo seu ID.
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable UUID id) {
        // @PathVariable pega o valor {id} da URL e o passa para o método.
        return livroService.buscarPorId(id)
                .map(livroEncontrado -> ResponseEntity.ok(livroEncontrado)) // Se encontrar, retorna 200 OK com o livro.
                .orElse(ResponseEntity.notFound().build()); // Se não encontrar, retorna 404 Not Found.
    }

    // POST /api/livros
    // Adiciona um novo livro.
    @PostMapping
    public ResponseEntity<Livro> adicionarLivro(@RequestBody Livro livro) {
        // @RequestBody converte o JSON enviado pelo frontend em um objeto Livro.
        Livro novoLivro = livroService.adicionarLivro(livro);

        // Cria a URL para o novo recurso criado (boa prática REST).
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoLivro.getId())
                .toUri();

        // Retorna 201 Created com a localização e o corpo do novo livro.
        return ResponseEntity.created(location).body(novoLivro);
    }

    // PUT /api/livros/{id}
    // Atualiza um livro existente.
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizarLivro(@PathVariable UUID id, @RequestBody Livro livroAtualizado) {
        // Garante que o ID do corpo da requisição seja o mesmo da URL.
        livroAtualizado.setId(id);
        
        try {
            Livro livroSalvo = livroService.atualizarLivro(livroAtualizado);
            return ResponseEntity.ok(livroSalvo); // Retorna 200 OK com o livro atualizado.
        } catch (RuntimeException e) {
            // Se o serviço lançar uma exceção (ex: livro não encontrado), retorna 404 Not Found.
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/livros/{id}
    // Exclui um livro.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLivro(@PathVariable UUID id) {
        livroService.excluirLivro(id);
        // Retorna 204 No Content, indicando sucesso na exclusão sem corpo de resposta.
        return ResponseEntity.noContent().build();
    }
}