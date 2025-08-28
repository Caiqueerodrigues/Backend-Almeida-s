package Development.Rodrigues.Almeidas_Cortes.commons.services;

import java.util.HashMap;
import java.util.Map;

public class MapConverterService {
    
    public static Map<String, Double> ConvertStringToObject(String input) {
        Map<String, Double> map = new HashMap<>();

        String[] pairs = input.split(",\\s*");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            
            if (keyValue.length == 2) {
                try {
                    String key = keyValue[0].trim();
                    Double value = Double.parseDouble(keyValue[1].trim());
                    map.put(key, value);
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao tentar converter a chave ou o valor: " + e.getMessage());
                }
            }
        }
        
        // Retorna o mapa
        return map;
    }
}
