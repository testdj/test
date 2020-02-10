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
@AllArgsConstructor
@NoArgsConstructor
public class CasopisDTO {

    private Long id;

    private String naziv;

    private String issn;

    private Long cena;

    private String koPlaca;

    @NotEmpty(message="At least one must be selected.")
    private List<NaucnaOblastDTO> naucneOblasti=new ArrayList<>();

    @NotEmpty(message="At least one must be selected.")
    private List<NacinPlacanjaDTO> naciniPlacanja=new ArrayList<>();

    private String taskId;

    private String processInstanceId;

    private Boolean enabled;

    private CasopisStatus casopisStatus = CasopisStatus.WAITING_FOR_INPUT;



}