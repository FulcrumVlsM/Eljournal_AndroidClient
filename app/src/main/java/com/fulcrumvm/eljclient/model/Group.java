package com.fulcrumvm.eljclient.model;

import java.util.List;

public class Group {
    public String ID;
    public String Name;
    public String Description;
    public String CuratorId;
    public String FacultyId;

    public List<Semester> Semesters;
}
