package me.huynhducphu.PingMe_Backend.controller.music;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.huynhducphu.PingMe_Backend.dto.request.music.SongRequest;
import me.huynhducphu.PingMe_Backend.dto.response.music.SongResponse;
import me.huynhducphu.PingMe_Backend.dto.response.music.SongResponseWithAllAlbum;
import me.huynhducphu.PingMe_Backend.service.music.SongService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(
        name = "Songs",
        description = "🎵 Quản lý bài hát: tìm kiếm, phát nhạc, upload, cập nhật, xóa & khôi phục"
)
@RestController
@RequestMapping("/songs")
@RequiredArgsConstructor
public class SongController {

    private final SongService songService;

    // ========================= GET BY ID =========================
    @Operation(
            summary = "Lấy chi tiết bài hát",
            description = "Trả về thông tin chi tiết của một bài hát theo ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lấy bài hát thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy bài hát")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SongResponse> getSongDetail(
            @Parameter(description = "ID bài hát", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    // ========================= GET ALL =========================
    @Operation(summary = "Lấy danh sách bài hát (phân trang)")
    @GetMapping("/all")
    public ResponseEntity<Page<SongResponseWithAllAlbum>> getAllSongs(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getAllSongs(pageable));
    }


    // ========================= SEARCH BY TITLE =========================
    @Operation(summary = "Tìm bài hát theo tên (phân trang)")
    @GetMapping("/search")
    public ResponseEntity<Page<SongResponse>> searchByTitle(
            @RequestParam String title,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getSongByTitle(title, pageable));
    }


    // ========================= SEARCH BY ALBUM =========================
    @Operation(summary = "Lấy bài hát theo album (phân trang)")
    @GetMapping("/search-by-album")
    public ResponseEntity<Page<SongResponseWithAllAlbum>> getByAlbum(
            @RequestParam("id") Long albumId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getSongByAlbum(albumId, pageable));
    }


    // ========================= SEARCH BY ARTIST =========================
    @Operation(summary = "Lấy bài hát theo nghệ sĩ (phân trang)")
    @GetMapping("/search-by-artist")
    public ResponseEntity<Page<SongResponseWithAllAlbum>> getByArtist(
            @RequestParam("id") Long artistId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getSongsByArtist(artistId, pageable));
    }


    // ========================= TOP PLAYED =========================
    @Operation(
            summary = "Lấy top bài hát nghe nhiều nhất",
            description = "Trả về danh sách bài hát có lượt nghe cao nhất"
    )
    @GetMapping("/getTopSong/{number}")
    public ResponseEntity<List<SongResponseWithAllAlbum>> getTopSongs(
            @Parameter(description = "Số lượng bài hát", example = "10")
            @PathVariable int number
    ) {
        return ResponseEntity.ok(songService.getTopPlayedSongs(number));
    }

    // ========================= SEARCH BY GENRE =========================
    @Operation(summary = "Lấy bài hát theo thể loại (phân trang)")
    @GetMapping("/genre")
    public ResponseEntity<Page<SongResponseWithAllAlbum>> getByGenre(
            @RequestParam("id") Long genreId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(songService.getSongByGenre(genreId, pageable));
    }


    // ========================= SAVE SONG =========================
    @Operation(
            summary = "Thêm bài hát mới",
            description = "Upload bài hát kèm file nhạc & ảnh bìa (multipart/form-data)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thêm bài hát thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ")
    })
    @PostMapping("/save")
    public ResponseEntity<List<SongResponse>> save(
            @Parameter(
                    description = "Thông tin bài hát",
                    content = @Content(schema = @Schema(implementation = SongRequest.class))
            )
            @Valid @RequestPart("songRequest") SongRequest songRequest,

            @Parameter(description = "File nhạc (.mp3, .wav)")
            @RequestPart("musicFile") MultipartFile musicFile,

            @Parameter(description = "Ảnh bìa bài hát")
            @RequestPart("imgFile") MultipartFile imgFile
    ) {
        return ResponseEntity.ok(songService.save(songRequest, musicFile, imgFile));
    }

    // ========================= UPDATE SONG =========================
    @Operation(
            summary = "Cập nhật bài hát",
            description = "Cập nhật thông tin bài hát, có thể thay file nhạc hoặc ảnh"
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<List<SongResponse>> update(
            @Parameter(description = "ID bài hát", example = "1")
            @PathVariable Long id,

            @Valid @RequestPart("songRequest") SongRequest songRequest,

            @RequestPart(value = "musicFile", required = false)
            MultipartFile musicFile,

            @RequestPart(value = "imgFile", required = false)
            MultipartFile imgFile
    ) throws IOException {
        return ResponseEntity.ok(songService.update(id, songRequest, musicFile, imgFile));
    }

    // ========================= SOFT DELETE =========================
    @Operation(summary = "Xóa mềm bài hát", description = "Ẩn bài hát khỏi hệ thống")
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        songService.softDelete(id);
        return ResponseEntity.ok().build();
    }

    // ========================= HARD DELETE =========================
    @Operation(summary = "Xóa vĩnh viễn bài hát")
    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        songService.hardDelete(id);
        return ResponseEntity.ok().build();
    }

    // ========================= RESTORE =========================
    @Operation(summary = "Khôi phục bài hát đã xóa")
    @PutMapping("/restore/{id}")
    public ResponseEntity<Void> restore(@PathVariable Long id) {
        songService.restore(id);
        return ResponseEntity.ok().build();
    }

    // ========================= PLAY COUNT =========================
    @Operation(
            summary = "Tăng lượt nghe",
            description = "Tăng play count khi người dùng phát bài hát"
    )
    @PostMapping("/{id}/play")
    public ResponseEntity<Void> increasePlayCount(
            @Parameter(description = "ID bài hát", example = "1")
            @PathVariable Long id
    ) {
        songService.increasePlayCount(id);
        return ResponseEntity.ok().build();
    }
}
