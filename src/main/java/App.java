import UI.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("dependency-config.xml");

        Controller controller =  context.getBean(Controller.class);
        controller.run();

    }
}
