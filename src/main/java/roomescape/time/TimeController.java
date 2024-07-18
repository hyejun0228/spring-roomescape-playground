package roomescape.time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
public class TimeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TimeController() {}

    @GetMapping("/time")
    public String time() {
        return "time";
    }

    @GetMapping("/times")
    @ResponseBody
    public ResponseEntity<List<Time>> times() {
        String sql = "SELECT id, time FROM time";
        List<Time> times = jdbcTemplate.query(
                sql,
                (resultSet, rowNum) -> (
                        new Time(
                                resultSet.getLong("id"),
                                resultSet.getString("time")
                        )
                ));
        return ResponseEntity.ok(times);
    }

    @PostMapping("/times")
    public ResponseEntity<Time> create(@RequestBody Time time) {
        try {
            String sql = "INSERT INTO time (time) VALUES (?)";
            jdbcTemplate.update(sql, time.getTime());

            Long newTimeId = jdbcTemplate.queryForObject("SELECT count(*) from time", Long.class);
            time.setId(newTimeId);

            return ResponseEntity.created(URI.create("/times/" + newTimeId))
                    .body(time);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        String sql = "DELETE FROM time WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected == 0) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
