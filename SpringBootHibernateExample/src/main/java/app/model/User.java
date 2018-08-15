package app.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Task> tasks = new ArrayList<>();

    public User() {
        super();
    }

    public User(String name, String password, Set<String> roles) {
        super();
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public User(Long id, String name, String password, Set<String> roles) {
        super();
        this.id = id;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return String.format("Student [id=%s, name=%s, password=%s, roles=%s]", id, name, password, roles);
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
