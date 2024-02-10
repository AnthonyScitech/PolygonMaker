import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class ContainerButtonHandler implements ActionListener {
    ContainerFrame theApp;   // Reference to ContainerFrame object

    // ButtonHandler constructor
    ContainerButtonHandler(ContainerFrame app ) {
        theApp = app;
    }


    // The action performed method would determine what text input or button press events
    // you might have a single event handler instance where the action performed method determines
    // the source of the event, or you might have separate event handler instances.
    // You might have separate event handler classes for managing text input retrieval and button
    // press events.
    public void actionPerformed(ActionEvent e) {

    }
}

