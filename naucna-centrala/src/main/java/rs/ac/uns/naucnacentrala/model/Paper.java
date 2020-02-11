package rs.ac.uns.naucnacentrala.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paper {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naslov;

    private String pojmovi;

    private String apstrakt;

    private String pdf;

    @ManyToOne(fetch = FetchType.EAGER)
    private Casopis casopis;

    private Long naucnaOblastId;


}