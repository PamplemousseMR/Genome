package Main;

import Data.IDataBase;
import Data.Statistics;
import Data.Tuple;
import GUI.TabbedPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static GUI.InformationPanel.swagComponent;

class JDataBase {

    /**
     * Create component to display an IDataBase
     *
     * @param _data the data to display
     * @return the component
     */
    static JComponent createComponent(IDataBase _data) {

        JPanel info = new JPanel();
        info.setLayout(new GridLayout(1, 2));

        info.add(swagComponent(new JTextArea(getProperties(_data))));
        info.add(swagComponent(new JTextArea(getValues(_data))));

        TabbedPanel tabbed = new TabbedPanel();
        tabbed.add("General Informations", swagComponent(info));

        for (Map.Entry<Statistics.Type, Statistics> ent : _data.getStatistics().entrySet()) {
            JPanel stat = new JPanel();
            stat.setLayout(new BoxLayout(stat, BoxLayout.Y_AXIS));
            swagComponent(stat);

            JPanel triPanel = new JPanel();
            triPanel.setLayout(new GridLayout(Statistics.Trinucleotide.values().length + 1, Statistics.StatLong.values().length + Statistics.StatFloat.values().length + 1));
            swagComponent(triPanel);

            triPanel.add(swagComponent(new JPanel()));
            triPanel.add(swagComponent(new JLabel(Statistics.StatLong.PHASE0.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatFloat.FREQ0.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatLong.PHASE1.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatFloat.FREQ1.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatLong.PHASE2.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatFloat.FREQ2.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatLong.PREF0.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatLong.PREF1.toString())));
            triPanel.add(swagComponent(new JLabel(Statistics.StatLong.PREF2.toString())));
            for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {
                triPanel.add(swagComponent(new JLabel(tri.toString())));
                Tuple row = ent.getValue().getTriTable()[tri.ordinal()];
                triPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PHASE0)))));
                triPanel.add(swagComponent(new JLabel(String.format("%.2f", row.get(Statistics.StatFloat.FREQ0) * 100) + "%")));
                triPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PHASE1)))));
                triPanel.add(swagComponent(new JLabel(String.format("%.2f", row.get(Statistics.StatFloat.FREQ1) * 100) + "%")));
                triPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PHASE2)))));
                triPanel.add(swagComponent(new JLabel(String.format("%.2f", row.get(Statistics.StatFloat.FREQ2) * 100) + "%")));
                triPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PREF0)))));
                triPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PREF1)))));
                triPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PREF2)))));
            }

            JPanel diPanel = new JPanel();
            diPanel.setLayout(new GridLayout(Statistics.Dinucleotide.values().length + 1, Statistics.StatLong.values().length + Statistics.StatFloat.values().length - 2));
            swagComponent(diPanel);

            diPanel.add(swagComponent(new JPanel()));
            diPanel.add(swagComponent(new JLabel(Statistics.StatLong.PHASE0.toString())));
            diPanel.add(swagComponent(new JLabel(Statistics.StatFloat.FREQ0.toString())));
            diPanel.add(swagComponent(new JLabel(Statistics.StatLong.PHASE1.toString())));
            diPanel.add(swagComponent(new JLabel(Statistics.StatFloat.FREQ1.toString())));
            diPanel.add(swagComponent(new JLabel(Statistics.StatLong.PREF0.toString())));
            diPanel.add(swagComponent(new JLabel(Statistics.StatLong.PREF1.toString())));
            for (Statistics.Dinucleotide di : Statistics.Dinucleotide.values()) {
                diPanel.add(swagComponent(new JLabel(di.toString())));
                Tuple row = ent.getValue().getDiTable()[di.ordinal()];
                diPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PHASE0)))));
                diPanel.add(swagComponent(new JLabel(String.format("%.2f", row.get(Statistics.StatFloat.FREQ0) * 100) + "%")));
                diPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PHASE1)))));
                diPanel.add(swagComponent(new JLabel(String.format("%.2f", row.get(Statistics.StatFloat.FREQ1) * 100) + "%")));
                diPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PREF0)))));
                diPanel.add(swagComponent(new JLabel(String.valueOf(row.get(Statistics.StatLong.PREF1)))));
            }

            stat.add(swagComponent(triPanel));
            stat.add(swagComponent(new JLabel(" ")));
            stat.add(swagComponent(diPanel));
            tabbed.add("TOTAL_" + ent.getKey().toString(), stat);
        }

        return tabbed;
    }

    /**
     * Get title string
     *
     * @return the title string
     */
    private static String getProperties(IDataBase _data) {
        StringBuilder res = new StringBuilder();
        res.append("Name\n");
        res.append("Total number of CDS sequences\n");
        res.append("Number of valid CDS\n");
        res.append("Number of invalid CDS\n");
        res.append("Number of organism\n");
        res.append("\n");

        for (Map.Entry<Statistics.Type, Long> ent : _data.getGenomeNumber().entrySet()) {
            res.append(ent.getKey()).append("\n");
        }
        res.append("\n");

        return res.toString();
    }

    /**
     * Get value string
     *
     * @return the value string
     */
    private static String getValues(IDataBase _data) {
        StringBuilder res = new StringBuilder();
        res.append(_data.getName()).append("\n");
        res.append(_data.getCDSNumber()).append("\n");
        res.append(_data.getValidCDSNumber()).append("\n");
        res.append(_data.getCDSNumber() - _data.getValidCDSNumber()).append("\n");
        res.append(_data.getTotalOrganism()).append("\n");
        res.append("\n");

        for (Map.Entry<Statistics.Type, Long> ent : _data.getGenomeNumber().entrySet()) {
            res.append(ent.getValue()).append("\n");
        }

        return res.toString();
    }

}
