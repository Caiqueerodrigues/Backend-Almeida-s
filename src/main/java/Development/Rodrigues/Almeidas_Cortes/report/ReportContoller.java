package Development.Rodrigues.Almeidas_Cortes.report;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.report.dto.ParamsFiltersReports;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/report")
public class ReportContoller {

    @Autowired
    ReportService service;

    @PostMapping("/generate")
    public ResponseEntity generateReport(@RequestBody @Valid ParamsFiltersReports dados) {
        try {
            ResponseDTO response = service.generateReportService(dados);
            if (response.response() != "") {
                String nomeFile = dados.report() + ".pdf";
                return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + nomeFile + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(response.response());
            } else {
                return ResponseEntity.status(200).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" + e, "", ""));
        }
    }
}
