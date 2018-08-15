package app;

import app.model.Task;
import app.model.User;
import app.repository.TaskRepository;
import app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@SpringBootApplication
public class BootApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepository repository;
    @Autowired
    TaskRepository taskRepository;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

    //This method defined in the CommandLineRunner interface is executed as soon as the application is launched up.
    @Override
    public void run(String... args) throws Exception {

        logger.info("Find user with id=1L ( expected not to be found):" + repository.findById(1L));
        logger.info("Find all users ( expected not to be found)" + repository.findAll());

        //Creating users
        repository.save(new User("John", "A1234657", new HashSet<>(Arrays.asList("admin", "user"))));
        repository.save(new User("Name", "A1234657", new HashSet<>(Arrays.asList("admin", "user"))));
        //Updating user
        repository.save(new User(2L, "Name-Updated", "New-Passport", new HashSet<>(Arrays.asList("user"))));

        //Creating task
        Task task = taskRepository.save(new Task("Say Hello", "This is your first task to say hello"));
        //Adding task to John user
        User performer = repository.findById(1L).get();
        performer.setTasks(Arrays.asList(task));
        repository.save(performer);

        List<User> usersList = repository.findAll();
        logger.info("John task is:" + usersList.get(0).getTasks());
        logger.info("All users: " + usersList);
    }
}
