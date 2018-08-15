package app.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TASK")
public class Task {

    @Id
    @GeneratedValue
    private Long id;
    private String topic;
    private String description;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER)
    private List<User> performers = new ArrayList<>();

    public Task() {
        super();
    }

    public Task(String topic, String description) {
        super();
        this.topic = topic;
        this.description = description;
    }

    public Task(Long id, String topic, String description) {
        super();
        this.id = id;
        this.topic = topic;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getPerformers() {
        return performers;
    }

    public void setPerformers(List<User> performers) {
        this.performers = performers;
    }

}
