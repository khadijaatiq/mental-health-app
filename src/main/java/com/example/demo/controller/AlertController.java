import com.example.demo.model.Alert;
import com.example.demo.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        alertService.deleteAlert(id);
    }
}
