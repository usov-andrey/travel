package org.humanhelper.travel.transport;

/**
 * @author Андрей
 * @since 08.05.14
 */
public class Transport {

    public static final String NAME_FIELD = "name";
    public static final String COMPANY_FIELD = "company";

    private String name;
    private String company;

    public Transport() {
    }

    /**
     * Иногда названия транспортного средства нет
     */
    public Transport(String company) {
        this.company = company;
    }

    public Transport(String name, String company) {
        this.name = name;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transport)) return false;

        Transport transport = (Transport) o;

        if (name != null ? !name.equals(transport.name) : transport.name != null) return false;
        return !(company != null ? !company.equals(transport.company) : transport.company != null);

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if (name == null) {
            return company;
        }
        return name + ", " + company;
    }

}
