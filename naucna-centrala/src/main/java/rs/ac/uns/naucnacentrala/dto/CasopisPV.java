package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasopisPV {

    private Long id;

    private String issn;

    private String naziv;

    private Long cena;

    private String processInstanceId;

    private String koPlaca;

    private CasopisStatus casopisStatus;

    private Boolean enabled;

    private String glavniUrednik;

    private LinkedHashMap<String, ArrayList<String>> recezenti = new LinkedHashMap<>();

    private LinkedHashMap<String, String> urednici = new LinkedHashMap<>();

}
