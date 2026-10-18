package user_service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    public record User(Long id, String name, String email) {}

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idSeq = new AtomicLong();

    @GetMapping
    public Collection<User> all() {
        return users.values();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        User user = users.get(id);
        return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
    }

    @PostMapping
    public User create(@RequestBody User request) {
        long id = idSeq.incrementAndGet();
        User saved = new User(id, request.name(), request.email());
        users.put(id, saved);
        return saved;
    }
}