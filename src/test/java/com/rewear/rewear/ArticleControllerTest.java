package com.rewear.rewear;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.User;
import com.rewear.rewear.entity.enums.ArticleStatus;
import com.rewear.rewear.entity.enums.Category;
import com.rewear.rewear.entity.enums.ItemCondition;
import com.rewear.rewear.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User mockUser() {
        User user = new User();
        user.setId(1);
        user.setName("Ana");
        user.setEmail("ana@test.com");
        user.setPoints(3);
        user.setIsAdult(true);
        return user;
    }

    private Article mockArticle() {
        Article article = new Article();
        article.setId(1);
        article.setTitle("Camiseta");
        article.setDescription("Bonita");
        article.setArticleStatus(ArticleStatus.DISPONIBLE);
        article.setItemCondition(ItemCondition.NUEVO);
        article.setCategory(Category.CAMISETAS);
        article.setUser(mockUser());
        return article;
    }

    @Test
    void obtenerArticulos_devuelve200() throws Exception {
        when(articleRepository.findAll(any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(mockArticle())));

        mockMvc.perform(get("/api/articles"))
            .andExpect(status().isOk());
    }

    @Test
    void obtenerArticuloPorId_devuelve200() throws Exception {
        when(articleRepository.findById(1)).thenReturn(Optional.of(mockArticle()));

        mockMvc.perform(get("/api/articles/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Camiseta"));
    }

    @Test
    void obtenerArticuloInexistente_devuelve404() throws Exception {
        when(articleRepository.findById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/articles/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void eliminarArticulo_devuelve204() throws Exception {
        when(articleRepository.existsById(1)).thenReturn(true);
        doNothing().when(articleRepository).deleteById(1);

        mockMvc.perform(delete("/api/articles/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void obtenerArticulosPorUsuario_devuelve200() throws Exception {
        when(articleRepository.findByUserId(1)).thenReturn(List.of(mockArticle()));

        mockMvc.perform(get("/api/articles/user/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title").value("Camiseta"));
    }
}
