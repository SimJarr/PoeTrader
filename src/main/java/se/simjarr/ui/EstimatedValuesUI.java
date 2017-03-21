package se.simjarr.ui;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;


public class EstimatedValuesUI extends GridLayout {


    public EstimatedValuesUI() {
        this.addStyleName("outlined");
        this.setWidth("220px");
        this.setHeight("320px");
        this.setMargin(true);
        generateMatrixGrid(3, 7);

        addEstimatedValuesSection();
    }

    private void addEstimatedValuesSection(){
        for (int i = 0; i <= 6; i++){
            ((Label)this.getComponent(0, i)).setValue("<img style='width:30px; height:30px;'src='http://currency.poe.trade/static/currency/Chaos_Orb.png'/>");
            ((Label)this.getComponent(2, i)).setValue("<img style='width:30px; height:30px;'src='http://currency.poe.trade/static/currency/Orb_of_Alteration.png'/>");
        }
    }

    private void generateMatrixGrid(final int columns, final int rows) {
        this.removeAllComponents();
        this.setRows(rows);
        this.setColumns(columns);

        for (int row = 0; row < this.getRows(); row++) {
            for (int col = 0; col < this.getColumns(); col++) {
                final Label child = new Label("1:17", ContentMode.HTML);
                this.addComponent(child);
                this.setComponentAlignment(child, Alignment.MIDDLE_CENTER);
                this.setRowExpandRatio(row, 0.0f);
                this.setColumnExpandRatio(col, 0.0f);
            }
        }
    }
}
