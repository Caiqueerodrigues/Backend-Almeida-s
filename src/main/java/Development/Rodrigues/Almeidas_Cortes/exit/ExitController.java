package Development.Rodrigues.Almeidas_Cortes.exit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exit")
public class ExitController {
    @Autowired
    ExitService service;

}
