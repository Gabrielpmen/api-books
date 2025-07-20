package br.com.gabs.mybooks_api.service; // <-- CORRIGIDO

import br.com.gabs.mybooks_api.model.Livro; // <-- CORRIGIDO
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

// @Service: Marca esta classe como um "serviço" do Spring.
// Isso permite que o Spring a gerencie e a injete em outras classes, como o nosso controller.
@Service
public class LivroService {

    // ObjectMapper é a ferramenta principal da biblioteca Jackson para converter Java <-> JSON.
    private final ObjectMapper objectMapper;

    // Define o caminho do nosso "banco de dados" JSON.
    // Ele será salvo na pasta do usuário que está executando a aplicação (ex: C:\Users\SeuUsuario\livros.json).
    private static final String FILE_PATH = System.getProperty("user.home") + "/livros.json";

    // O construtor é chamado pelo Spring.
    public LivroService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // Configura o ObjectMapper para escrever o JSON de forma legível (indentado).
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    // Carrega a lista de livros do arquivo JSON.
    public List<Livro> carregarLivros() {
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                return new CopyOnWriteArrayList<>(); // Usa uma lista segura para concorrência.
            }
            String json = Files.readString(Paths.get(FILE_PATH));
            if (json.isEmpty()) {
                return new CopyOnWriteArrayList<>();
            }
            // Usa TypeReference para converter o JSON em uma lista de objetos Livro.
            return objectMapper.readValue(json, new TypeReference<CopyOnWriteArrayList<Livro>>() {});
        } catch (IOException e) {
            // Em uma aplicação real, aqui teríamos um log de erro mais robusto.
            throw new RuntimeException("Falha ao carregar os livros do arquivo.", e);
        }
    }

    // Salva a lista inteira de livros no arquivo JSON.
    private void salvarLivros(List<Livro> livros) {
        try {
            String json = objectMapper.writeValueAsString(livros);
            Files.writeString(Paths.get(FILE_PATH), json);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar os livros no arquivo.", e);
        }
    }
    
    // Busca um único livro pelo seu ID.
    public Optional<Livro> buscarPorId(UUID id) {
        return carregarLivros().stream()
                .filter(livro -> livro.getId().equals(id))
                .findFirst();
    }

    // Adiciona um novo livro à lista e salva.
    public Livro adicionarLivro(Livro novoLivro) {
        List<Livro> livros = carregarLivros();
        livros.add(novoLivro);
        salvarLivros(livros);
        return novoLivro;
    }

    // Atualiza um livro existente na lista.
    public Livro atualizarLivro(Livro livroAtualizado) {
        List<Livro> livros = carregarLivros();
        // Procura pelo livro com o mesmo ID
        Optional<Livro> livroExistente = livros.stream()
                .filter(l -> l.getId().equals(livroAtualizado.getId()))
                .findFirst();

        if (livroExistente.isPresent()) {
            // Remove o antigo e adiciona o novo para garantir a atualização
            livros.remove(livroExistente.get());
            livros.add(livroAtualizado);
            salvarLivros(livros);
            return livroAtualizado;
        } else {
            // Lança uma exceção se o livro a ser atualizado não for encontrado.
            throw new RuntimeException("Livro com ID " + livroAtualizado.getId() + " não encontrado.");
        }
    }

    // Exclui um livro da lista pelo ID.
    public void excluirLivro(UUID id) {
        List<Livro> livros = carregarLivros();
        // Remove da lista o livro que tem o ID correspondente.
        boolean removed = livros.removeIf(livro -> livro.getId().equals(id));
        if (removed) {
            salvarLivros(livros);
        } else {
             throw new RuntimeException("Livro com ID " + id + " não encontrado para exclusão.");
        }
    }
}