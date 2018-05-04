package com.fulcrumvm.eljclient.model;

import java.util.List;

public class User {
    public String ID;
    public String Surname;
    public String Name;
    public String Patronymic;
    public String Info;
    public String RoleId;

    public List<Department> Departments;
    public List<Faculty> Faculties;
}
