package Main;

import Data.IDataBase;
import Data.Statistics;
import Data.Tuple;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static GUI.InformationPanel.swagComponent;

public class JDataBase {

    protected static JComponent createComponent(IDataBase _data) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        swagComponent(panel);

        JPanel info = new JPanel();
        info.setLayout(new GridLayout(1, 2));
        swagComponent(info);
        info.add(swagComponent(new JTextArea(getProperties(_data))));
        info.add(swagComponent(new JTextArea(getValues(_data))));

        panel.add(info);

        JPanel stats = new JPanel();
        stats.setLayout(new GridLayout(_data.getStatistics().size(), 1));
        swagComponent(info);

        for (Map.Entry<Statistics.Type, Statistics> ent : _data.getStatistics().entrySet()) {
            JPanel stat = new JPanel();
            stat.setLayout(new BoxLayout(stat, BoxLayout.Y_AXIS));
            swagComponent(stat);

            stat.add(swagComponent(new JLabel(ent.getKey().toString())));

            JPanel arr = new JPanel();
            arr.setLayout(new GridLayout(Statistics.Trinucleotide.values().length + 1, Statistics.StatLong.values().length + Statistics.StatFloat.values().length));
            swagComponent(arr);

            arr.add(swagComponent(new JLabel()));
            for (Statistics.StatFloat fl : Statistics.StatFloat.values()) {
                arr.add(swagComponent(new JLabel(fl.toString())));
            }
            for (Statistics.StatLong lo : Statistics.StatLong.values()) {
                arr.add(swagComponent(new JLabel(lo.toString())));
            }
            for (Statistics.Trinucleotide tri : Statistics.Trinucleotide.values()) {
                arr.add(swagComponent(new JLabel(tri.toString())));
                Tuple row = ent.getValue().getTriTable()[tri.ordinal()];
                for (Statistics.StatFloat fl : Statistics.StatFloat.values()) {
                    arr.add(swagComponent(new JLabel(String.valueOf(row.get(fl)))));
                }
                for (Statistics.StatLong lo : Statistics.StatLong.values()) {
                    arr.add(swagComponent(new JLabel(String.valueOf(row.get(lo)))));
                }
            }

            stat.add(arr);

            stats.add(stat);
        }

        panel.add(stats);
        return panel;
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
    public static String getValues(IDataBase _data) {
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
