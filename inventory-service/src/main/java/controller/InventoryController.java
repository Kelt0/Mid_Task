package controller;

import dto.UpdateStockRequest;
import entity.Stock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.StockRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final StockRepository stockRepository;

    public InventoryController(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * Получение остатка по ID товара. Доступно всем.
     * Эндпоинт: GET /api/inventory/{productId}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Stock> getStock(@PathVariable String productId) {
        return stockRepository.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Пополнение запасов (инвентаризация). Доступно только ADMIN.
     * Эндпоинт: POST /api/inventory/increase
     */
    @PostMapping("/increase")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> increaseStock(@RequestBody UpdateStockRequest request) {
        Optional<Stock> existingStock = stockRepository.findByProductId(request.getProductId());
        Stock stock;

        if (existingStock.isPresent()) {
            stock = existingStock.get();
            stock.setQuantity(stock.getQuantity() + request.getQuantity());
        } else {
            stock = new Stock(request.getProductId(), request.getQuantity());
        }

        stockRepository.save(stock);

        return new ResponseEntity<>("Stock increased successfully for product: " + request.getProductId(), HttpStatus.OK);
    }

}
