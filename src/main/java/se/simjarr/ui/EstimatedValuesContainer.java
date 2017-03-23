package se.simjarr.ui;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

public class EstimatedValuesContainer extends VerticalLayout{

    private EstimatedValuesList estimatedValuesList;

    public EstimatedValuesContainer() {
        addUpdateButton();
        addEstValuesList();
        this.setMargin(false);
    }

    private void addUpdateButton(){
        Button button = new Button();
        button.setSizeFull();
        button.setWidth("250px");
        button.setCaption("Update Values");
        button.addClickListener(clickEvent -> estimatedValuesList.fetchRatios());
        this.addComponent(button);
    }

    private void addEstValuesList(){
        estimatedValuesList = new EstimatedValuesList();
        this.addComponent(estimatedValuesList);
    }
}
