package com.mySampleApplication.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.DOM;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import java.util.ArrayList;

/**
 * Entry point classes define <code>onModuleLoad()</code>
 */
public class MySampleApplication implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        FlexTable table = new FlexTable();
        final Button addToTableButton = new Button("Add to the Table");
        Label insert_label = new Label("Insert 3 sides of the triangle");
        TextBox textBox_a = new TextBox();
        TextBox textBox_b = new TextBox();
        TextBox textBox_c = new TextBox();
        Button deleteButton = new Button("Delete Rows");

        VerticalPanel verticalPanel1 = new VerticalPanel();
        verticalPanel1.add(insert_label);
        verticalPanel1.add(textBox_a);
        verticalPanel1.add(textBox_b);
        verticalPanel1.add(textBox_c);
        verticalPanel1.add(addToTableButton);
        verticalPanel1.add(deleteButton);

        String[] columnNames = new String[]{"&#10003", "A", "B", "C", "Perimeter", "Square"};
        for (int i = 0; i < columnNames.length; i++)
            table.setHTML(0, i, columnNames[i]);

        addToTableButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                try {
                    double a = new Double(textBox_a.getText());
                    double b = new Double(textBox_b.getText());
                    double c = new Double(textBox_c.getText());

                    if ((a > 0) && (b > 0) && (c > 0)) {
                        if (((a+b) > c)&&((c+b) > a)&&((a+c) > b)) {
                            //Window.alert("Good!");
                            int row_index = table.getRowCount();
                            CheckBox checkBox = new CheckBox();
                            table.setWidget(row_index, 0, checkBox);
                            String[] sRow = new String[]{String.valueOf(a), String.valueOf(b), String.valueOf(c)};
                            for (int i = 0; i < sRow.length; i++)
                                table.setText(row_index, i + 1, sRow[i]);

                            Button pButton = new Button("Perimeter");
                            Button sButton = new Button("Square");

                            pButton.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    Node row = pButton.getElement().getParentNode().getParentNode();

                                    double aa = Double.parseDouble(((Element) row.getChild(1)).getInnerText());
                                    double bb = Double.parseDouble(((Element) row.getChild(2)).getInnerText());
                                    double cc = Double.parseDouble(((Element) row.getChild(3)).getInnerText());

                                    String result = String.valueOf(aa + bb + cc);
                                    ((Element) row.getChild(4)).setInnerHTML(result);

                                }
                            });

                            sButton.addClickHandler(new ClickHandler() {
                                @Override
                                public void onClick(ClickEvent event) {
                                    Node row = sButton.getElement().getParentNode().getParentNode();

                                    double aa = Double.parseDouble(((Element) row.getChild(1)).getInnerText());
                                    double bb = Double.parseDouble(((Element) row.getChild(2)).getInnerText());
                                    double cc = Double.parseDouble(((Element) row.getChild(3)).getInnerText());

                                    double p = (aa + bb + cc) / 2;
                                    String result = String.valueOf(Math.sqrt(p * (p - aa) * (p - bb) * (p - cc)));
                                    ((Element) row.getChild(5)).setInnerHTML(result);
                                }
                            });

                            table.setWidget(row_index, 4, pButton);
                            table.setWidget(row_index, 5, sButton);
                        } else {
                            Window.alert("Incorrect data! Such triangle doesn't exist. Try again");
                        }
                    } else
                        Window.alert("Incorrect data! All numbers have to be greater than zero. Try again");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Window.alert("Incorrect data! Try again");
                }
            }
        });


        deleteButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                for (int i = 1; i < table.getRowCount(); i++) {
                    boolean status = ((Element) table.getElement().getLastChild().getChild(i).getFirstChild().getFirstChild().getFirstChild()).getPropertyBoolean("checked");
                    if (status) {
                        table.removeRow(i);
                        i--;
                    }
                }
            }

        });

        RootPanel.get("slot1").add(verticalPanel1);
        RootPanel.get("slot2").add(table);
    }


    private static class MyAsyncCallback implements AsyncCallback<String> {
        private Label label;

        public MyAsyncCallback(Label label) {
            this.label = label;
        }

        public void onSuccess(String result) {
            label.getElement().setInnerHTML(result);
        }

        public void onFailure(Throwable throwable) {
            label.setText("Failed to receive answer from server!");
        }
    }
}
