package rs.ac.uns.naucnacentrala.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FormSubmissionDto implements Serializable{

	Object fieldValue;
	String fieldId;

	
	public FormSubmissionDto() {
		super();
	}

	public FormSubmissionDto(String fieldId, String fieldValue) {
		super();
		this.fieldValue = fieldValue;
		this.fieldId = fieldId;
	}

	
	
}
