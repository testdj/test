package rs.ac.uns.naucnacentrala.dto;

import org.camunda.bpm.engine.form.FormField;

import java.util.List;

public class FormFieldsDto {

	String processInstanceId;
	List<FormField> formFields;
	String taskId;

	public FormFieldsDto(String taskId, String processInstanceId, List<FormField> formFields) {
		super();
		this.taskId = taskId;
		this.processInstanceId = processInstanceId;
		this.formFields = formFields;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public FormFieldsDto() {
		super();
	}

	public String getTaskId() {
		return taskId;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public List<FormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<FormField> formFields) {
		this.formFields = formFields;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	
}
