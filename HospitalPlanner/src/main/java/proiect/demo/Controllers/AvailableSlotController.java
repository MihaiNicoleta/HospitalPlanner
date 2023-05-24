package proiect.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import proiect.demo.Domain.AvailableSlot;
import proiect.demo.Services.AvailableSlotService;

import java.util.List;

@RestController
@RequestMapping("/available_slots")
public class AvailableSlotController {

    @Autowired
    private AvailableSlotService availableSlotService;

    @GetMapping
    public ResponseEntity<List<AvailableSlot>> getAllAvailableSlots() {
        List<AvailableSlot> availableSlots = availableSlotService.getAllAvailableSlots();
        return new ResponseEntity<>(availableSlots, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailableSlot> getAvailableSlotById(@PathVariable int id) {
        AvailableSlot availableSlot = availableSlotService.getAvailableSlotById(id);
        return new ResponseEntity<>(availableSlot, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<AvailableSlot> updateAvailableSlot(@PathVariable int id, @RequestBody AvailableSlot availableSlot) {
        AvailableSlot updatedAvailableSlot = availableSlotService.updateAvailableSlot(id, availableSlot);
        return new ResponseEntity<>(updatedAvailableSlot, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteAvailableSlot(@PathVariable int id) {
        availableSlotService.deleteAvailableSlot(id);
    }
}