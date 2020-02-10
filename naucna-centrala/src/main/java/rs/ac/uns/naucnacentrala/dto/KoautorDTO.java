package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KoautorDTO {

    private String ime;

    private String email;

    private String prezime;

    private String grad;

    private String drzava;

    private String adresa;
}
