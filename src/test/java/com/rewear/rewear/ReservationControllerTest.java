package com.rewear.rewear;

import com.rewear.rewear.entity.Article;
import com.rewear.rewear.entity.Reservation;
import com.rewear.rewear.entity.User;
import com.rewear.rewear.entity.enums.ArticleStatus;
import com.rewear.rewear.entity.enums.ItemCondition;
import com.rewear.rewear.entity.enums.Category;
import com.rewear.rewear.repository.ReservationRepository;
import com.rewear.rewear.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private ReservationRepository reservationRepository;

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
        article.setArticleStatus(ArticleStatus.RESERVADO);
        article.setItemCondition(ItemCondition.NUEVO);
        article.setCategory(Category.CAMISETAS);
        article.setUser(mockUser());
        return article;
    }

    private Reservation mockReservation() {
        Reservation r = new Reservation();
        r.setId(1);
        r.setArticle(mockArticle());
        r.setUser(mockUser());
        r.setReservedAt(LocalDateTime.now());
        r.setExpiresAt(LocalDateTime.now().plusHours(24));
        return r;
    }

    @Test
    void crearReserva_devuelve201() throws Exception {
        when(reservationService.createReservation(1, 1)).thenReturn(mockReservation());

        mockMvc.perform(post("/api/reservations")
                .param("articleId", "1")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminarReserva_devuelve204() throws Exception {
        doNothing().when(reservationService).deleteReservation(1);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelarPorArticulo_devuelve204() throws Exception {
        when(reservationRepository.findByArticleId(1)).thenReturn(Optional.of(mockReservation()));
        doNothing().when(reservationService).deleteReservation(anyInt());

        mockMvc.perform(delete("/api/reservations/article/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void confirmarIntercambio_devuelve204() throws Exception {
        doNothing().when(reservationService).confirmExchange(1);

        mockMvc.perform(put("/api/reservations/1/confirm"))
                .andExpect(status().isNoContent());
    }

    @Test
    void obtenerReservasPorUsuario_devuelve200() throws Exception {
        when(reservationRepository.findByUserId(1)).thenReturn(List.of(mockReservation()));

        mockMvc.perform(get("/api/reservations/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
