package com.example.webservice_practice.v1;

import com.example.webservice_practice.dto.ItemDto;
import com.example.webservice_practice.request.ItemCreateRequest;
import com.example.webservice_practice.request.ItemUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    // 메모리용 임시 DB (예제용)
    private final Map<Long, ItemDto> store = new HashMap<>();
    private long sequence = 1L;

    // 1) GET: 전체 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getItems() {
        return success(200, new ArrayList<>(store.values()));
    }

    // 2) GET: 단건 조회 (PathVariable 사용)
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getItem(@PathVariable Long id) {
        ItemDto item = store.get(id);
        if (item == null) {
            return fail(404, "해당 상품을 찾을 수 없습니다.");
        }
        return success(200, item);
    }

    // 1) POST: 생성
    @PostMapping
    public ResponseEntity<Map<String, Object>> createItem(@RequestBody ItemCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return fail(400, "상품 이름은 필수입니다.");
        }
        if (request.getPrice() == null || request.getPrice() < 0) {
            return fail(400, "가격은 0 이상이어야 합니다.");
        }

        ItemDto item = new ItemDto();
        item.setId(sequence++);
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setLikes(0);

        store.put(item.getId(), item);

        return success(201, item);
    }
    // 2) POST:
    @PostMapping("/{id}/likes")
    public ResponseEntity<Map<String, Object>> addLike(@PathVariable Long id) {
        ItemDto item = store.get(id);
        if (item == null) {
            return fail(404, "상품을 찾을 수 없습니다.");
        }
        item.setLikes(item.getLikes() + 1);
        return success(200, item);
    }
    // 1) PUT: 수정
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateRequest request
    ) {
        ItemDto item = store.get(id);
        if (item == null) {
            return fail(404, "상품을 찾을 수 없습니다.");
        }

        if (request.getName() != null) {
            item.setName(request.getName());
        }
        if (request.getPrice() != null) {
            item.setPrice(request.getPrice());
        }

        return success(200, item);
    }
    // 2) PUT: 재고 수정
    @PutMapping("/{id}/stock")
    public ResponseEntity<Map<String, Object>> updateStock(@PathVariable Long id) {
        if (!store.containsKey(id)) {
            return fail(404, "상품을 찾을 수 없습니다.");
        }
        return fail(503, "재고 시스템 점검 중입니다. 잠시 후 다시 시도해주세요.");
    }

    // 1) DELETE: 상품 삭제함.
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteItem(@PathVariable Long id) {
        ItemDto removed = store.remove(id);
        if (removed == null) {
            return fail(404, "상품을 찾을 수 없습니다.");
        }
        return success(200, null);
    }
    // 2) DELETE: 전체 삭제
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteAllItems() {
        return fail(500, "서버 처리 중 오류가 발생했습니다.");
    }

    private ResponseEntity<Map<String, Object>> success(int code, Object data) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "success");
        body.put("data", data);
        return ResponseEntity.status(code).body(body);
    }

    private ResponseEntity<Map<String, Object>> fail(int code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("statusCode", code);
        error.put("message", message);

        Map<String, Object> body = new HashMap<>();
        body.put("status", "error");
        body.put("data", error);
        return ResponseEntity.status(code).body(body);
    }
}
