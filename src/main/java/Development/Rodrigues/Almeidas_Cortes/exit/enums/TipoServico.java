package Development.Rodrigues.Almeidas_Cortes.exit.enums;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoServico {
    Corte("Corte"),
    Debruagem("Debruagem"),
    Dublagem("Dublagem"),
    Geral("Geral"),
    Material("Material"),
    Montagem("Montagem"),
    Pesponto("Pesponto"),
    PespontoDuasAgulhas("Pesponto 2 Agulhas"),
    Vies("Vies"),
    Waster("Waster");

    private final String descricao;

    TipoServico(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static TipoServico fromValue(String value) {
        return Arrays.stream(values())
            .filter(tipo -> tipo.descricao.equalsIgnoreCase(value) || tipo.name().equalsIgnoreCase(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Tipo de serviço inválido: " + value));
    }

    @Override
    public String toString() {
        return descricao;
    }
}
