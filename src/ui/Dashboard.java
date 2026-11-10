
package ui;

import attacks.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import logs.SecurityLogger;
import network.*;
import security.*;
import threats.ThreatManager;

public class Dashboard extends JFrame {

    // ==============================
    // COLORS
    // ==============================

    private final Color BG = new Color(15, 25, 36);
    private final Color PANEL = new Color(27, 42, 55);
    private final Color GREEN = new Color(46, 190, 130);
    private final Color WHITE = new Color(240, 245, 248);
    private final Color MUTED = new Color(160, 175, 188);

    // ==============================
    // GUI COMPONENTS
    // ==============================

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private DefaultListModel<String> logModel;
    private DefaultTableModel threatModel;
    private DefaultTableModel networkModel;

    private JLabel networkHealthValue;
    private JLabel activeThreatsValue;
    private JLabel blockedValue;
    private JLabel defenderScoreValue;

    // ==============================
    // BACKEND OBJECTS
    // ==============================

    private NetworkManager networkManager = new NetworkManager();
    private AttackManager attackManager = new AttackManager();
    private Firewall firewall = new Firewall();
    private IntrusionDetector detector = new IntrusionDetector();
    private ThreatManager threatManager = new ThreatManager();
    private SecurityLogger securityLogger = new SecurityLogger();

    private int blockedCount = 0;
    private int detectedCount = 0;
    private int allowedCount = 0;

    // ==============================
    // CONSTRUCTOR
    // ==============================

    public Dashboard() {

        setTitle("CyberGuard - Attack Defense Simulator");
        setSize(1200, 750);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        logModel = new DefaultListModel<>();

        threatModel = new DefaultTableModel(
            new String[]{
                "Attack",
                "Target",
                "Severity",
                "Firewall Status",
                "Incident Status"
            },
            0
        );

        networkModel = new DefaultTableModel(
            new String[]{
                "Device ID",
                "Device Name",
                "Type",
                "Status"
            },
            0
        );

        loadInitialData();

        add(createSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BG);

        mainPanel.add(createDashboardPanel(), "Dashboard");
        mainPanel.add(createNetworkPanel(), "Network");
        mainPanel.add(createAttackPanel(), "Attacks");
        mainPanel.add(createThreatPanel(), "Threats");
        mainPanel.add(createLogsPanel(), "Logs");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "Dashboard");

        refreshDashboardStats();
    }

    // ==============================
    // INITIAL NETWORK DATA
    // ==============================

    private void loadInitialData() {

        NetworkDevice server =
            new Server("S101", "Main Server");

        NetworkDevice router =
            new Router("R101", "Main Router");

        NetworkDevice database =
            new Database("D101", "User Database");

        networkManager.addDevice(server);
        networkManager.addDevice(router);
        networkManager.addDevice(database);

        for (NetworkDevice device : networkManager.getDevices()) {

            String type = "";

            if (device instanceof Server) {
                type = "Server";
            }
            else if (device instanceof Router) {
                type = "Router";
            }
            else if (device instanceof Database) {
                type = "Database";
            }

            networkModel.addRow(new Object[]{
                device.getDeviceId(),
                device.getDeviceName(),
                type,
                device.getStatus()
            });
        }

        logModel.addElement("System initialized.");
        logModel.addElement("Virtual network created.");
        logModel.addElement("Network devices loaded successfully.");
    }

    // ==============================
    // SIDEBAR
    // ==============================

    private JPanel createSidebar() {

        JPanel sidebar = new JPanel();

        sidebar.setPreferredSize(new Dimension(225, 0));
        sidebar.setBackground(new Color(19, 31, 44));

        sidebar.setLayout(
            new BoxLayout(sidebar, BoxLayout.Y_AXIS)
        );

        sidebar.setBorder(
            BorderFactory.createEmptyBorder(25, 15, 20, 15)
        );

        JLabel logo = new JLabel("  CYBERGUARD");

        logo.setForeground(GREEN);
        logo.setFont(new Font("Arial", Font.BOLD, 23));

        JLabel subtitle =
            new JLabel("  ATTACK-DEFENSE SIMULATOR");

        subtitle.setForeground(MUTED);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 9));

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(subtitle);
        sidebar.add(Box.createVerticalStrut(45));

        addNavButton(sidebar, "Dashboard", "Dashboard");
        addNavButton(sidebar, "Network Management", "Network");
        addNavButton(sidebar, "Attack Simulator", "Attacks");
        addNavButton(sidebar, "Threat Management", "Threats");
        addNavButton(sidebar, "Security Logs", "Logs");

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("  JAVA OOPS PROJECT");

        footer.setForeground(MUTED);
        footer.setFont(new Font("Arial", Font.PLAIN, 10));

        sidebar.add(footer);

        return sidebar;
    }

    private void addNavButton(
        JPanel sidebar,
        String text,
        String screen
    ) {

        JButton button = new JButton(text);

        button.setMaximumSize(new Dimension(200, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(new Color(19, 31, 44));
        button.setForeground(WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e ->
            cardLayout.show(mainPanel, screen)
        );

        sidebar.add(button);
        sidebar.add(Box.createVerticalStrut(10));
    }

    // ==============================
    // COMMON PANEL
    // ==============================

    private JPanel createBasePanel(String title) {

        JPanel panel = new JPanel(
            new BorderLayout(15, 20)
        );

        panel.setBackground(BG);

        panel.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        JLabel heading = new JLabel(title);

        heading.setForeground(WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 25));

        panel.add(heading, BorderLayout.NORTH);

        return panel;
    }

    // ==============================
    // DASHBOARD STAT CARD
    // ==============================

    private JLabel createStatCard(
        JPanel container,
        String title,
        String value
    ) {

        JPanel card = new JPanel();

        card.setBackground(PANEL);

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel heading = new JLabel(title);

        heading.setForeground(MUTED);
        heading.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel number = new JLabel(value);

        number.setForeground(GREEN);
        number.setFont(new Font("Arial", Font.BOLD, 30));

        card.add(heading);
        card.add(Box.createVerticalStrut(12));
        card.add(number);

        container.add(card);

        return number;
    }

    // ==============================
    // DASHBOARD SCREEN
    // ==============================

    private JPanel createDashboardPanel() {

        JPanel panel = createBasePanel("Security Dashboard");

        JPanel content = new JPanel(
            new BorderLayout(15, 20)
        );

        content.setBackground(BG);

        JPanel stats = new JPanel(
            new GridLayout(1, 4, 15, 0)
        );

        stats.setBackground(BG);

        networkHealthValue =
            createStatCard(stats, "Network Health", "100%");

        activeThreatsValue =
            createStatCard(stats, "Active Threats", "0");

        blockedValue =
            createStatCard(stats, "Blocked Attacks", "0");

        defenderScoreValue =
            createStatCard(stats, "Defender Score", "850");

        JPanel recent = new JPanel(
            new BorderLayout(10, 10)
        );

        recent.setBackground(PANEL);

        recent.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel recentTitle =
            new JLabel("Recent Security Events");

        recentTitle.setForeground(WHITE);
        recentTitle.setFont(
            new Font("Arial", Font.BOLD, 18)
        );

        JList<String> recentList = new JList<>(logModel);

        recentList.setBackground(PANEL);
        recentList.setForeground(WHITE);
        recentList.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        recent.add(recentTitle, BorderLayout.NORTH);
        recent.add(new JScrollPane(recentList), BorderLayout.CENTER);

        content.add(stats, BorderLayout.NORTH);
        content.add(recent, BorderLayout.CENTER);

        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    // ==============================
    // DASHBOARD STATISTICS
    // ==============================

    private void refreshDashboardStats() {

        int totalDevices = networkManager.getDevices().size();

        int activeDevices = 0;
        int isolatedDevices = 0;

        for (NetworkDevice device : networkManager.getDevices()) {

            if (device.getStatus().equalsIgnoreCase("ACTIVE")) {
                activeDevices++;
            }
            else if (device.getStatus().equalsIgnoreCase("ISOLATED")) {
                isolatedDevices++;
            }
        }

        int networkHealth = 0;

        if (totalDevices > 0) {
            networkHealth = (activeDevices * 100) / totalDevices;
        }

        int defenderScore = 850
            + (blockedCount * 10)
            - (allowedCount * 25)
            - (isolatedDevices * 50);

        defenderScore = Math.max(
            0,
            Math.min(1000, defenderScore)
        );

        networkHealthValue.setText(networkHealth + "%");

        activeThreatsValue.setText(
            String.valueOf(threatManager.getActiveThreatCount())
        );

        blockedValue.setText(
            String.valueOf(blockedCount)
        );

        defenderScoreValue.setText(
            String.valueOf(defenderScore)
        );
    }

    // ==============================
    // NETWORK MANAGEMENT
    // ==============================

    private JPanel createNetworkPanel() {

        JPanel panel = createBasePanel("Network Management");

        JTable table = new JTable(networkModel);

        styleTable(table);

        JButton isolate = createButton("Isolate Device");
        JButton recover = createButton("Recover Device");

        isolate.addActionListener(e ->
            changeDeviceStatus(table, "ISOLATED")
        );

        recover.addActionListener(e ->
            changeDeviceStatus(table, "ACTIVE")
        );

        JPanel actions = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        actions.setBackground(BG);

        actions.add(isolate);
        actions.add(recover);

        JPanel content = new JPanel(
            new BorderLayout(10, 15)
        );

        content.setBackground(BG);

        content.add(
            new JScrollPane(table),
            BorderLayout.CENTER
        );

        content.add(actions, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    private void changeDeviceStatus(
        JTable table,
        String status
    ) {

        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                this,
                "Please select a device first."
            );

            return;
        }

        int modelRow =
            table.convertRowIndexToModel(selectedRow);

        String deviceId = (String)
            networkModel.getValueAt(modelRow, 0);

        NetworkDevice device =
            networkManager.findDevice(deviceId);

        if (device == null) {

            JOptionPane.showMessageDialog(
                this,
                "Device not found!"
            );

            return;
        }

        device.setStatus(status);

        networkModel.setValueAt(
            device.getStatus(),
            modelRow,
            3
        );

        String event = device.getDeviceName()
            + " status changed to "
            + device.getStatus();

        logModel.addElement(event);
        securityLogger.logEvent(event);

        refreshDashboardStats();

        JOptionPane.showMessageDialog(
            this,
            device.getDeviceName()
            + " is now " + status
        );
    }

    // ==============================
    // ATTACK SIMULATOR
    // ==============================

    private JPanel createAttackPanel() {

        JPanel panel = createBasePanel("Attack Simulator");

        JPanel form = new JPanel();

        form.setBackground(PANEL);

        form.setLayout(
            new BoxLayout(form, BoxLayout.Y_AXIS)
        );

        form.setBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        JLabel attackLabel =
            new JLabel("Select Attack Type");

        attackLabel.setForeground(WHITE);

        String[] attacks = {
            "Phishing",
            "Brute Force",
            "Malware",
            "Traffic Overload"
        };

        JComboBox<String> attackBox =
            new JComboBox<>(attacks);

        JLabel targetLabel =
            new JLabel("Select Target Device");

        targetLabel.setForeground(WHITE);

        JComboBox<String> targetBox =
            new JComboBox<>(
                new String[]{
                    "Main Server",
                    "Main Router",
                    "User Database"
                }
            );

        JButton launch =
            createButton("Launch Simulation");

        launch.addActionListener(e -> {

            String attackName =
                (String) attackBox.getSelectedItem();

            String target =
                (String) targetBox.getSelectedItem();

            Attack attack = null;

            switch (attackName) {

                case "Phishing":
                    attack = new PhishingAttack(target);
                    break;

                case "Brute Force":
                    attack = new BruteForceAttack(target);
                    break;

                case "Malware":
                    attack = new MalwareAttack(target);
                    break;

                case "Traffic Overload":
                    attack = new TrafficOverloadAttack(target);
                    break;
            }

            if (attack == null) {
                return;
            }

            attackManager.addAttack(attack);

            String result = attack.execute();

            boolean detected = detector.detect(attack);

            boolean blocked = firewall.shouldBlock(attack);

            String status = blocked ? "BLOCKED" : "ALLOWED";

            if (detected) {

                threatManager.addThreat(attack);

                threatModel.addRow(new Object[]{
                    attack.getAttackName(),
                    attack.getTarget(),
                    attack.getSeverity(),
                    status,
                    "PENDING"
                });
            }

            if (blocked) {
                blockedCount++;
            }

            if (!blocked) {
                allowedCount++;
            }

            if (detected) {
                detectedCount++;
            }

            String event = attack.getAttackName()
                + " | Target: " + target
                + " | Status: " + status
                + " | Detected: " + detected;

            securityLogger.logEvent(event);
            logModel.addElement(event);

            refreshDashboardStats();

            JOptionPane.showMessageDialog(
                this,
                "SIMULATION COMPLETED!\n\n"
                + "Attack: " + attackName
                + "\nTarget: " + target
                + "\nSeverity: " + attack.getSeverity()
                + "\nIDS Detected: " + detected
                + "\nFirewall Status: " + status
                + "\n\nResult: " + result
            );
        });

        form.add(attackLabel);
        form.add(Box.createVerticalStrut(10));
        form.add(attackBox);
        form.add(Box.createVerticalStrut(25));

        form.add(targetLabel);
        form.add(Box.createVerticalStrut(10));
        form.add(targetBox);
        form.add(Box.createVerticalStrut(30));
        form.add(launch);

        panel.add(form, BorderLayout.CENTER);

        return panel;
    }

    // ==============================
    // THREAT MANAGEMENT
    // ==============================

    private JPanel createThreatPanel() {

        JPanel panel = createBasePanel("Threat Management");

        // SUMMARY CARDS

        JPanel summary = new JPanel(
            new GridLayout(1, 4, 12, 0)
        );

        summary.setBackground(BG);

        JLabel totalValue = new JLabel("0");
        JLabel pendingValue = new JLabel("0");
        JLabel investigatingValue = new JLabel("0");
        JLabel resolvedValue = new JLabel("0");

        summary.add(
            createThreatStatCard("TOTAL THREATS", totalValue)
        );

        summary.add(
            createThreatStatCard("PENDING", pendingValue)
        );

        summary.add(
            createThreatStatCard("INVESTIGATING", investigatingValue)
        );

        summary.add(
            createThreatStatCard("RESOLVED", resolvedValue)
        );

        // THREAT TABLE

        JTable table = new JTable(threatModel);

        styleTable(table);

        table.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        table.setRowHeight(42);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);

        table.setIntercellSpacing(
            new Dimension(0, 1)
        );

        TableRowSorter<DefaultTableModel> sorter =
            new TableRowSorter<>(threatModel);

        table.setRowSorter(sorter);

        // SEVERITY COLORS

        table.getColumnModel().getColumn(2)
            .setCellRenderer(new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(
                    JTable t,
                    Object value,
                    boolean selected,
                    boolean focus,
                    int row,
                    int column
                ) {

                    JLabel label = (JLabel)
                        super.getTableCellRendererComponent(
                            t, value, selected, focus, row, column
                        );

                    label.setHorizontalAlignment(
                        SwingConstants.CENTER
                    );

                    if (!selected) {

                        String severity =
                            String.valueOf(value);

                        if (severity.equalsIgnoreCase("CRITICAL")) {
                            label.setForeground(
                                new Color(255, 90, 90)
                            );
                        }
                        else if (severity.equalsIgnoreCase("HIGH")) {
                            label.setForeground(
                                new Color(255, 150, 90)
                            );
                        }
                        else if (severity.equalsIgnoreCase("MEDIUM")) {
                            label.setForeground(
                                new Color(255, 205, 90)
                            );
                        }
                        else {
                            label.setForeground(
                                new Color(90, 200, 150)
                            );
                        }
                    }

                    label.setFont(
                        new Font("Arial", Font.BOLD, 12)
                    );

                    return label;
                }
            });

        // INCIDENT STATUS COLORS

        table.getColumnModel().getColumn(4)
            .setCellRenderer(new DefaultTableCellRenderer() {

                @Override
                public Component getTableCellRendererComponent(
                    JTable t,
                    Object value,
                    boolean selected,
                    boolean focus,
                    int row,
                    int column
                ) {

                    JLabel label = (JLabel)
                        super.getTableCellRendererComponent(
                            t, value, selected, focus, row, column
                        );

                    label.setHorizontalAlignment(
                        SwingConstants.CENTER
                    );

                    if (!selected) {

                        String status =
                            String.valueOf(value);

                        if (status.equals("PENDING")) {
                            label.setForeground(
                                new Color(255, 190, 90)
                            );
                        }
                        else if (status.equals("INVESTIGATING")) {
                            label.setForeground(
                                new Color(90, 175, 255)
                            );
                        }
                        else if (status.equals("RESOLVED")) {
                            label.setForeground(
                                new Color(70, 210, 145)
                            );
                        }
                    }

                    label.setFont(
                        new Font("Arial", Font.BOLD, 12)
                    );

                    return label;
                }
            });

        // SEARCH FIELD

        JTextField searchField = new JTextField(20);

        searchField.setBackground(PANEL);
        searchField.setForeground(WHITE);
        searchField.setCaretColor(GREEN);

        searchField.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        searchField.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(55, 75, 90)
                ),
                BorderFactory.createEmptyBorder(
                    9, 12, 9, 12
                )
            )
        );

        // STATUS FILTER

        JComboBox<String> statusFilter =
            new JComboBox<>(
                new String[]{
                    "All Status",
                    "PENDING",
                    "INVESTIGATING",
                    "RESOLVED"
                }
            );

        statusFilter.setBackground(PANEL);
        statusFilter.setForeground(WHITE);

        // APPLY SEARCH AND FILTER

        Runnable applyFilters = () -> {

            ArrayList<RowFilter<DefaultTableModel, Integer>>
                filters = new ArrayList<>();

            String search =
                searchField.getText().trim();

            if (!search.isEmpty()) {

                filters.add(
                    RowFilter.regexFilter(
                        "(?i)" + Pattern.quote(search)
                    )
                );
            }

            String selectedStatus =
                (String) statusFilter.getSelectedItem();

            if (selectedStatus != null
                && !selectedStatus.equals("All Status")) {

                filters.add(
                    RowFilter.regexFilter(
                        "^" + Pattern.quote(selectedStatus) + "$",
                        4
                    )
                );
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            }
            else {
                sorter.setRowFilter(
                    RowFilter.andFilter(filters)
                );
            }
        };

        searchField.getDocument().addDocumentListener(
            new DocumentListener() {

                public void insertUpdate(DocumentEvent e) {
                    applyFilters.run();
                }

                public void removeUpdate(DocumentEvent e) {
                    applyFilters.run();
                }

                public void changedUpdate(DocumentEvent e) {
                    applyFilters.run();
                }
            }
        );

        statusFilter.addActionListener(
            e -> applyFilters.run()
        );

        JPanel filterPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 12, 10)
        );

        filterPanel.setBackground(BG);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(WHITE);

        JLabel filterLabel = new JLabel("Status:");
        filterLabel.setForeground(WHITE);

        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(filterLabel);
        filterPanel.add(statusFilter);

        // INCIDENT DETAILS

        JPanel detailsPanel = new JPanel();

        detailsPanel.setLayout(
            new BoxLayout(detailsPanel, BoxLayout.Y_AXIS)
        );

        detailsPanel.setBackground(PANEL);

        detailsPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(55, 75, 90)
                ),
                BorderFactory.createEmptyBorder(
                    15, 18, 15, 18
                )
            )
        );

        JLabel detailsTitle =
            new JLabel("INCIDENT DETAILS");

        detailsTitle.setForeground(GREEN);

        detailsTitle.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        JTextArea detailsText = new JTextArea(
            "Select a threat from the table to view its details."
        );

        detailsText.setBackground(PANEL);
        detailsText.setForeground(WHITE);

        detailsText.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        detailsText.setEditable(false);
        detailsText.setLineWrap(true);
        detailsText.setWrapStyleWord(true);

        detailsPanel.add(detailsTitle);
        detailsPanel.add(Box.createVerticalStrut(12));
        detailsPanel.add(detailsText);

        // DETAILS REFRESH HELPER

        Runnable updateDetails = () -> {

            int selected = table.getSelectedRow();

            if (selected != -1) {

                int modelRow =
                    table.convertRowIndexToModel(selected);

                detailsText.setText(
                    "Attack: "
                    + threatModel.getValueAt(modelRow, 0)

                    + "\n\nTarget: "
                    + threatModel.getValueAt(modelRow, 1)

                    + "\n\nSeverity: "
                    + threatModel.getValueAt(modelRow, 2)

                    + "\n\nFirewall Status: "
                    + threatModel.getValueAt(modelRow, 3)

                    + "\n\nIncident Status: "
                    + threatModel.getValueAt(modelRow, 4)
                );
            }
            else {
                detailsText.setText(
                    "Select a threat from the table to view its details."
                );
            }
        };

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {
                updateDetails.run();
            }
        });

        // ACTION BUTTONS

        JButton investigate =
            createButton("Investigate Threat");

        JButton resolve =
            createButton("Resolve Threat");

        // INVESTIGATE BUTTON

        investigate.addActionListener(e -> {

            int selected = table.getSelectedRow();

            if (selected == -1) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please select a threat first."
                );

                return;
            }

            int modelRow =
                table.convertRowIndexToModel(selected);

            boolean success =
                threatManager.investigateThreat(modelRow);

            if (!success) {

                JOptionPane.showMessageDialog(
                    this,
                    "This threat is already resolved."
                );

                return;
            }

            threatModel.setValueAt(
                "INVESTIGATING",
                modelRow,
                4
            );

            updateDetails.run();

            String event =
                "Threat investigation started: "
                + threatModel.getValueAt(modelRow, 0);

            securityLogger.logEvent(event);
            logModel.addElement(event);

            refreshDashboardStats();

            JOptionPane.showMessageDialog(
                this,
                "Threat is now under investigation."
            );
        });

        // RESOLVE BUTTON

        resolve.addActionListener(e -> {

            int selected = table.getSelectedRow();

            if (selected == -1) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please select a threat first."
                );

                return;
            }

            int modelRow =
                table.convertRowIndexToModel(selected);

            boolean success =
                threatManager.resolveThreat(modelRow);

            if (!success) {

                JOptionPane.showMessageDialog(
                    this,
                    "This threat is already resolved."
                );

                return;
            }

            threatModel.setValueAt(
                "RESOLVED",
                modelRow,
                4
            );

            updateDetails.run();

            String event =
                "Threat resolved: "
                + threatModel.getValueAt(modelRow, 0);

            securityLogger.logEvent(event);
            logModel.addElement(event);

            refreshDashboardStats();

            JOptionPane.showMessageDialog(
                this,
                "Threat successfully resolved!"
            );
        });

        JPanel actionPanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 12, 10)
        );

        actionPanel.setBackground(BG);

        actionPanel.add(investigate);
        actionPanel.add(resolve);

        // UPDATE SUMMARY CARDS

        Runnable updateSummary = () -> {

            int total = threatModel.getRowCount();

            int pending = 0;
            int investigating = 0;
            int resolved = 0;

            for (int i = 0; i < total; i++) {

                String status = String.valueOf(
                    threatModel.getValueAt(i, 4)
                );

                if (status.equals("PENDING")) {
                    pending++;
                }
                else if (status.equals("INVESTIGATING")) {
                    investigating++;
                }
                else if (status.equals("RESOLVED")) {
                    resolved++;
                }
            }

            totalValue.setText(String.valueOf(total));
            pendingValue.setText(String.valueOf(pending));

            investigatingValue.setText(
                String.valueOf(investigating)
            );

            resolvedValue.setText(
                String.valueOf(resolved)
            );
        };

        threatModel.addTableModelListener(
            e -> updateSummary.run()
        );

        updateSummary.run();

        // MAIN LAYOUT

        JPanel top = new JPanel(
            new BorderLayout(10, 10)
        );

        top.setBackground(BG);

        top.add(summary, BorderLayout.NORTH);
        top.add(filterPanel, BorderLayout.SOUTH);

        JPanel center = new JPanel(
            new BorderLayout(10, 12)
        );

        center.setBackground(BG);

        center.add(
            new JScrollPane(table),
            BorderLayout.CENTER
        );

        center.add(
            detailsPanel,
            BorderLayout.SOUTH
        );

        JPanel content = new JPanel(
            new BorderLayout(12, 15)
        );

        content.setBackground(BG);

        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(actionPanel, BorderLayout.SOUTH);

        panel.add(content, BorderLayout.CENTER);

        return panel;
    }

    // ==============================
    // THREAT SUMMARY CARD
    // ==============================

    private JPanel createThreatStatCard(
        String title,
        JLabel value
    ) {

        JPanel card = new JPanel();

        card.setLayout(
            new BoxLayout(card, BoxLayout.Y_AXIS)
        );

        card.setBackground(PANEL);

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(45, 65, 80)
                ),
                BorderFactory.createEmptyBorder(
                    15, 16, 15, 16
                )
            )
        );

        JLabel heading = new JLabel(title);

        heading.setForeground(MUTED);

        heading.setFont(
            new Font("Arial", Font.BOLD, 10)
        );

        value.setForeground(GREEN);

        value.setFont(
            new Font("Arial", Font.BOLD, 27)
        );

        card.add(heading);
        card.add(Box.createVerticalStrut(9));
        card.add(value);

        return card;
    }

    // ==============================
    // SECURITY LOGS
    // ==============================

    private JPanel createLogsPanel() {

        JPanel panel = createBasePanel("Security Logs");

        JList<String> logs = new JList<>(logModel);

        logs.setBackground(PANEL);
        logs.setForeground(WHITE);

        logs.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        logs.setSelectionBackground(GREEN);
        logs.setSelectionForeground(Color.BLACK);

        panel.add(
            new JScrollPane(logs),
            BorderLayout.CENTER
        );

        JButton refresh = createButton("Refresh Logs");

        refresh.addActionListener(e ->
            logs.setModel(logModel)
        );

        JPanel bottom = new JPanel(
            new FlowLayout(FlowLayout.LEFT)
        );

        bottom.setBackground(BG);
        bottom.add(refresh);

        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    // ==============================
    // COMMON BUTTON DESIGN
    // ==============================

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setBackground(GREEN);
        button.setForeground(Color.BLACK);

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        button.setPreferredSize(
            new Dimension(180, 40)
        );

        return button;
    }

    // ==============================
    // TABLE DESIGN
    // ==============================

    private void styleTable(JTable table) {

        table.setBackground(PANEL);
        table.setForeground(WHITE);
        table.setGridColor(BG);

        table.setRowHeight(35);

        table.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        table.setSelectionBackground(GREEN);
        table.setSelectionForeground(Color.BLACK);

        table.getTableHeader().setBackground(
            new Color(36, 57, 70)
        );

        table.getTableHeader().setForeground(WHITE);

        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );
    }
}
