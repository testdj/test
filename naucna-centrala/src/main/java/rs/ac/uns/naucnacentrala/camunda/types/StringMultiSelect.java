package rs.ac.uns.naucnacentrala.camunda.types;

import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.value.TypedValue;

import java.util.HashMap;
import java.util.Map;

public class StringMultiSelect extends AbstractFormFieldType {

    protected Map<String, String> values=new HashMap<>();
    private String TYPE_NAME;

    public StringMultiSelect(Map<String, String> values) {
        this.values = values;
    }
    public StringMultiSelect(String name){
        this.TYPE_NAME=name;

    };

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    public Map<String, String> getValues() {
        return this.values;
    }

    public Object getInformation(String key) {
        return key.equals("values") ? this.values : null;
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        return Variables.objectValue(typedValue.getValue(),false).create();
    }

    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        return Variables.objectValue(typedValue.getValue(),false).create();
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return null;
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return null;
    }
}
