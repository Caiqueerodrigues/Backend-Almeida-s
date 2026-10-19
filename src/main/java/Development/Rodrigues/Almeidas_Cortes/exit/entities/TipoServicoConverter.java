package Development.Rodrigues.Almeidas_Cortes.exit.entities;

import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoServicoConverter implements AttributeConverter<TipoServico, String> {

    @Override
    public String convertToDatabaseColumn(TipoServico tipoServico) {
        return tipoServico == null ? null : tipoServico.getDescricao();
    }

    @Override
    public TipoServico convertToEntityAttribute(String value) {
        return value == null ? null : TipoServico.fromValue(value);
    }
}
