package marrero_ferrera_gcid_ulpgc.view;

import marrero_ferrera_gcid_ulpgc.control.schemas.EnergyPrice;
import marrero_ferrera_gcid_ulpgc.model.Model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewSwing extends JFrame {

    private final DefaultTableModel tableModel;

    public ViewSwing(Model model) {
        setTitle("Tabla de Model");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LocalDateTime dateTime = LocalDateTime.ofInstant(model.getFinalItems().get(0).getPredictionTime(), ZoneId.systemDefault());
        String dayAndMonth = dateTime.format(DateTimeFormatter.ofPattern("MM-dd"));
        JPanel textPanel = new JPanel();
        JLabel textLabel = new JLabel("Your Prices and Production for Today (Battery Gained marks the production from the last weather received until now)");
        textPanel.add(textLabel);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Hour for today: " + dayAndMonth);
        tableModel.addColumn("Weather Type");
        tableModel.addColumn("Wind Gained (Kw)");
        tableModel.addColumn("Solar Gained (Kw)");
        tableModel.addColumn("Battery Gained (%)");
        tableModel.addColumn("Price (€)");
        tableModel.addColumn("Slot & Recommendation");

        fillTable(model.getFinalItems());
        JTable table = new JTable(tableModel);
        table.getColumnModel().getColumn(6).setCellRenderer(new RecommendationCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(textPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    private void fillTable(ArrayList<Model.Item> items) {
        for (Model.Item item : items) {
            LocalDateTime dateTime = LocalDateTime.ofInstant(item.getPredictionTime(), ZoneId.systemDefault());

            Object[] rowData = {
                    dateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                    item.getWeatherType(),
                    item.getWindGained(),
                    item.getSolarGained(),
                    " +" + (item.getBatteryGained() * 100) + "%",
                    item.getPrice(),
                    item.getSlot()
            };
            tableModel.addRow(rowData);
        }
    }

    private static class RecommendationCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            EnergyPrice.Slot slot = (EnergyPrice.Slot) value;
            if (slot == EnergyPrice.Slot.Valley) {
                component.setBackground(Color.GREEN);
            } else if (slot == EnergyPrice.Slot.Flat) {
                component.setBackground(Color.YELLOW);
            } else {
                component.setBackground(Color.RED);
            }
            return component;
        }
    }
}
