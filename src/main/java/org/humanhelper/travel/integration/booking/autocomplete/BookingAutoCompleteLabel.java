package org.humanhelper.travel.integration.booking.autocomplete;

/**
 * {"hl":1,"required":1,"text":"Bangkok","type":"city"}
 *
 * @author Андрей
 * @since 17.09.15
 */
public class BookingAutoCompleteLabel {

    private String type;
    private String text;
    private Integer hl;
    private Integer required;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getHl() {
        return hl;
    }

    public void setHl(Integer hl) {
        this.hl = hl;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    @Override
    public String toString() {
        return "BookingAutoCompleteLabel{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", hl=" + hl +
                ", required=" + required +
                '}';
    }
}
