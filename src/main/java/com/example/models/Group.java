package com.example.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "groups")
public class Group {

    @Id
    @SequenceGenerator(name = "group_seq", sequenceName = "group_seq", allocationSize = 1)
    @GeneratedValue(generator = "group_seq")
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "date_of_start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date dateOfStart;

    private String image;

    private int count;

    public void plusCount(){
        count++;
    }

    public void minusCount(){
        count--;
    }

    @ManyToOne(cascade = {PERSIST, MERGE, REFRESH, DETACH}, fetch = EAGER)
    private Company company;

    @ManyToMany(cascade = {MERGE, REFRESH, DETACH,PERSIST}, mappedBy = "groups")
    private List<Course> courses;
    public void addCourse(Course course){
        if (courses==null){
            courses=new ArrayList<>();
        }
        courses.add(course);
        plusCount();
    }

    @OneToMany(cascade = ALL, fetch = LAZY, mappedBy = "group")
    private List<Student> students;
    public void addStudent(Student student) {
        if (students == null) {
            students = new ArrayList<>();
        }
        students.add(student);
        this.getCompany().plusStudent();
    }

    public void assignStudent(Student student){
        if (students==null){
            students=new ArrayList<>();
        }
        students.add(student);
    }
}
