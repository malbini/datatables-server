package datatables.server;

import datatables.annotations.Column;

import java.util.Date;

public class TestObject {
    @Column("NAME")
    private String name;

    @Column("SURNAME")
    private String surname;

    @Column("BIRTHDATE")
    private Date birthDate;

    public TestObject(String name, String surname, Date birthDate) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
