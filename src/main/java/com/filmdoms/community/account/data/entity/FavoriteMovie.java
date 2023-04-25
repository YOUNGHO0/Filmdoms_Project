package com.filmdoms.community.account.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "favorite_movie", indexes = {
        @Index(columnList = "account_id"),
        @Index(columnList = "movie_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FavoriteMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Builder
    private FavoriteMovie(Account account, Movie movie) {
        this.account = account;
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FavoriteMovie that = (FavoriteMovie) o;
        return Objects.equals(account, that.account) && Objects.equals(movie,
                that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, movie);
    }
}
