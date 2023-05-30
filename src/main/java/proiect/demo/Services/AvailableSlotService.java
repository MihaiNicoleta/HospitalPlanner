package proiect.demo.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proiect.demo.Domain.AvailableSlot;
import proiect.demo.Repostiories.AvailableSlotRepository;
import proiect.demo.configs.ResourceNotFoundException;

import java.util.List;

@Service
public class AvailableSlotService {

    @Autowired
    private AvailableSlotRepository availableSlotRepository;

    public List<AvailableSlot> getAllAvailableSlots() {
        return availableSlotRepository.findAll();
    }

    public AvailableSlot getAvailableSlotById(int id) {
        return availableSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Available slot with id=" + id + " not found"));
    }

    public AvailableSlot addAvailableSlot(AvailableSlot availableSlot) {
        return availableSlotRepository.save(availableSlot);
    }

    public AvailableSlot updateAvailableSlot(int id, AvailableSlot availableSlot) {
        AvailableSlot existingAvailableSlot = availableSlotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Available slot with id=" + id + " not found"));
        existingAvailableSlot.setDay(availableSlot.getDay());
        existingAvailableSlot.setStartTime(availableSlot.getStartTime());
        existingAvailableSlot.setEndTime(availableSlot.getEndTime());
        return availableSlotRepository.save(existingAvailableSlot);
    }

    public void deleteAvailableSlot(int id) {
        availableSlotRepository.deleteById(id);
    }

}
