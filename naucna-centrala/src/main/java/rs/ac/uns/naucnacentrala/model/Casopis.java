package rs.ac.uns.naucnacentrala.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Casopis implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issn;

    @Column(unique = true)
    private String naziv;

    private Long cena;

    private String koPlaca;

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_nacinplacanja",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "nacinplacanja_id", referencedColumnName = "id"))
    private List<NacinPlacanja> naciniPlacanja=new ArrayList<NacinPlacanja>();

    @ManyToMany(fetch = FetchType.EAGER,cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_naucneoblasti",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "naucnaoblast_id", referencedColumnName = "id"))
    private List<NaucnaOblast> naucneOblasti=new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_urednici",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> urednici=new ArrayList<User>();

    @ManyToMany(cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JoinTable(name = "casopis_recezent",
            joinColumns = @JoinColumn(name = "casopis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> recezenti=new ArrayList<User>();



    private Boolean enabled=false;

    private String glavniUrednik;

    private String processInstanceId;

    @Enumerated(EnumType.STRING)
    private CasopisStatus casopisStatus;
}
