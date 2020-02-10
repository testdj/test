package rs.ac.uns.naucnacentrala.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.naucnacentrala.model.CasopisStatus;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CasopisDTO {

    private Long id;

    private String issn;

    private String naziv;

    private String koPlaca;

    private Long cena;

    @NotEmpty(message="At least one must be selected.")
    private List<NaucnaOblastDTO> naucneOblasti=new ArrayList<>();

    @NotEmpty(message="At least one must be selected.")
    private List<NacinPlacanjaDTO> naciniPlacanja=new ArrayList<>();

    private Boolean enabled;

    private String processInstanceId;

    private String taskId;

    private CasopisStatus casopisStatus = CasopisStatus.WAITING_FOR_INPUT;

}