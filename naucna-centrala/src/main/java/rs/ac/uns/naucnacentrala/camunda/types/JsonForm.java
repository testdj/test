package rs.ac.uns.naucnacentrala.camunda.types;


import org.camunda.bpm.engine.impl.form.type.AbstractFormFieldType;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.PrimitiveTypeValueImpl;
import org.camunda.bpm.engine.variable.value.StringValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.camunda.spin.json.SpinJsonNode;
import org.camunda.spin.plugin.variable.SpinValues;
import org.camunda.spin.plugin.variable.value.JsonValue;
import org.camunda.spin.plugin.variable.value.SpinValue;

import static org.camunda.spin.Spin.JSON;

public class JsonForm extends AbstractFormFieldType {

    private String TYPE_NAME;

    public JsonForm(String name){
        this.TYPE_NAME=name;
    }

    @Override
    public TypedValue convertToFormValue(TypedValue typedValue) {
        StringValue ret=Variables.stringValue("[]");
        return ret;
    }

    @Override
    public Object convertFormValueToModelValue(Object o) {
        return null;
    }

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    @Override
    public String convertModelValueToFormValue(Object o) {
        return null;
    }

    @Override
    public TypedValue convertToModelValue(TypedValue typedValue) {
        return typedValue;
    }
}
