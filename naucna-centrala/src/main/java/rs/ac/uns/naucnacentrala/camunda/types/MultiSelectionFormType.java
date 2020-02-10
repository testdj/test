package rs.ac.uns.naucnacentrala.camunda.types;


import lombok.NoArgsConstructor;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.type.SerializableValueType;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class MultiSelectionFormType extends AbstractFormFieldType {

    protected Map<Long, String> values=new HashMap<>();
    private String TYPE_NAME;

    public MultiSelectionFormType(Map<Long, String> values) {
        this.values = values;
    }
    public MultiSelectionFormType(String name){
        this.TYPE_NAME=name;

    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    public Map<Long, String> getValues() {
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
