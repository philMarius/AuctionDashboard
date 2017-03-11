package Views;

import Controllers.DashboardMainFrameController;
import Model.DBEnums.LogType;
import Views.CustomComponents.CatPanel;
import Views.StartupPanels.RecentProjectsViewPanel;
import Views.StartupPanels.StartupChosenFilesPanel;
import Views.StartupPanels.StartupFileImportPanel;
import Views.ViewPresets.ColorSettings;
import Views.ViewPresets.FontSettings;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

//TODO
//TODO Need to create and configure a startup controller
//TODO

/**
 * DashboardStartupFrame is the first frame the user sees, containing the elements needed to load in files
 */
public class DashboardStartupFrame extends JFrame {

    private File homedir;
    private Map<LogType, File> fileMap;
    private StartupChosenFilesPanel viewPanel;
    private DashboardMainFrameController controller;
    private boolean loading;

    public DashboardStartupFrame(File homedir, DashboardMainFrameController controller) {
        this.homedir = homedir;
        fileMap = new HashMap<>();
        this.controller = controller;
    }

    public void initStartup() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error getting System Look and Feel, reverting to Java default",
                    "Graphical Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        this.setSize(new Dimension(1250, 600));
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        CatPanel contentPane = new CatPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;

                if (loading) {
                    g2.setColor(ColorSettings.LOADING_COLOR.getColor());
                    g2.fillRect(0, 0, this.getWidth(), this.getHeight());
                }
            }
        };
        contentPane.setLayout(new BorderLayout());
        this.setContentPane(contentPane);

        CatPanel centrePanel = new CatPanel();
        centrePanel.setLayout(new GridBagLayout());

        StartupFileImportPanel importPanel = new StartupFileImportPanel(homedir);
        GridBagConstraints importPanelConstraints = new GridBagConstraints();
        importPanelConstraints.gridx = 1;
        importPanelConstraints.weightx = 10;
        importPanelConstraints.weighty = 1;
        importPanelConstraints.fill = GridBagConstraints.BOTH;

        viewPanel = new StartupChosenFilesPanel();
        GridBagConstraints viewPanelConstraints = new GridBagConstraints();
        viewPanelConstraints.gridx = 2;
        viewPanelConstraints.weightx = 1;
        viewPanelConstraints.weighty = 1;
        viewPanelConstraints.fill = GridBagConstraints.BOTH;

        RecentProjectsViewPanel recentProjects = new RecentProjectsViewPanel(null);
        GridBagConstraints recentProjectsConstraints = new GridBagConstraints();
        recentProjectsConstraints.gridx = 0;
        recentProjectsConstraints.weightx = 1;
        recentProjectsConstraints.weighty = 1;
        recentProjectsConstraints.fill = GridBagConstraints.BOTH;

        centrePanel.add(recentProjects, recentProjectsConstraints);
        centrePanel.add(importPanel, importPanelConstraints);
        centrePanel.add(viewPanel, viewPanelConstraints);

        contentPane.add(centrePanel, BorderLayout.CENTER);
        this.initGlassPane();
        this.setVisible(true);
    }

    public void fileChosen(LogType logType, File file) {
        this.fileMap.put(logType, file);
        this.viewPanel.setLogPanelName(logType, file);
    }

    public void sumbitFiles() {
        this.controller.processFiles(this.fileMap);
    }

    public void initGlassPane() {
        JPanel loadingPanel = (JPanel) this.getGlassPane();
        loadingPanel.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("img/animal.gif");
        icon.setImage(icon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
        JLabel loadingLabel = new JLabel(icon);

        JLabel textLoadingLabel = new JLabel("Loading...");
        textLoadingLabel.setFont(FontSettings.LOADING_FONT.getFont());
        textLoadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        textLoadingLabel.setVerticalAlignment(SwingConstants.CENTER);

        JPanel textLoadingPanel = new JPanel();
        textLoadingPanel.setLayout(new BoxLayout(textLoadingPanel, BoxLayout.X_AXIS));
        textLoadingPanel.add(Box.createRigidArea(new Dimension(0, 100)));
        textLoadingPanel.add(Box.createHorizontalGlue());
        textLoadingPanel.add(textLoadingLabel);
        textLoadingPanel.add(Box.createHorizontalGlue());
        textLoadingPanel.setOpaque(false);

        loadingPanel.add(loadingLabel, BorderLayout.CENTER);
        loadingPanel.add(textLoadingPanel, BorderLayout.SOUTH);
    }

    public void displayLoading() {
        this.loading = true;
        this.getGlassPane().setVisible(true);
        this.getContentPane().setEnabled(false);
        repaint();
    }

    public void finishedLoading() {
        this.loading = false;
        this.getGlassPane().setVisible(false);

        JOptionPane.showMessageDialog(this, "Data Loaded!",
                "Success! Your data has been loaded into the database!", JOptionPane.INFORMATION_MESSAGE);


        this.setEnabled(true);
        repaint();
    }
}