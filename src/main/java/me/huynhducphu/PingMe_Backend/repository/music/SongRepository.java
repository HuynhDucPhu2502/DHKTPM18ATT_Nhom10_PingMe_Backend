package me.huynhducphu.PingMe_Backend.repository.music;

import me.huynhducphu.PingMe_Backend.model.music.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    // ================= GET BY ID (GIỮ NGUYÊN) =================
    @Query("""
        SELECT s FROM Song s
        LEFT JOIN FETCH s.artistRoles ar
        LEFT JOIN FETCH ar.artist
        LEFT JOIN FETCH s.genres
        LEFT JOIN FETCH s.albums
        WHERE s.id = :id
    """)
    Optional<Song> findByIdWithDetails(@Param("id") Long id);

    // ================= PLAY COUNT =================
    @Modifying
    @Transactional
    @Query("UPDATE Song s SET s.playCount = s.playCount + 1 WHERE s.id = :id")
    void incrementPlayCount(@Param("id") Long id);

    // ================= GET ALL (PAGINATION) =================
    @Query("""
        SELECT s FROM Song s
        WHERE s.isDeleted = false
    """)
    Page<Song> findAllActive(Pageable pageable);

    // ================= SEARCH BY TITLE =================
    Page<Song> findByTitleContainingIgnoreCaseAndIsDeletedFalse(
            String title,
            Pageable pageable
    );

    // ================= SEARCH BY GENRE =================
    @Query("""
        SELECT DISTINCT s FROM Song s
        JOIN s.genres g
        WHERE g.id = :genreId
          AND s.isDeleted = false
    """)
    Page<Song> findByGenreId(
            @Param("genreId") Long genreId,
            Pageable pageable
    );

    // ================= SEARCH BY ALBUM =================
    @Query("""
        SELECT DISTINCT s FROM Album a
        JOIN a.songs s
        WHERE a.id = :albumId
          AND s.isDeleted = false
    """)
    Page<Song> findByAlbumId(
            @Param("albumId") Long albumId,
            Pageable pageable
    );

    // ================= SEARCH BY ARTIST =================
    @Query("""
        SELECT DISTINCT sar.song
        FROM SongArtistRole sar
        WHERE sar.artist.id = :artistId
          AND sar.song.isDeleted = false
    """)
    Page<Song> findByArtistId(
            @Param("artistId") Long artistId,
            Pageable pageable
    );

    // ================= TOP PLAYED (GIỮ LIST) =================
    @Query("SELECT s FROM Song s ORDER BY s.playCount DESC")
    List<Song> findSongsByPlayCount(Pageable pageable);

    // ================= SOFT DELETE SUPPORT =================
    @Query(value = "SELECT * FROM songs WHERE id = :id AND is_deleted = true", nativeQuery = true)
    Optional<Song> findSoftDeletedSong(@Param("id") Long id);

    @Query(value = "SELECT * FROM songs WHERE id = :id", nativeQuery = true)
    Optional<Song> findByIdIgnoringDeleted(@Param("id") Long id);
}
